<template>
  <a-modal v-model="show" title="商品详情" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="onClose" type="danger">
        关闭
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="commodityData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">商品信息</span></a-col>
        <a-col :span="8"><b>商品编号：</b>
          {{ commodityData.code }}
        </a-col>
        <a-col :span="8"><b>商品名称：</b>
          <a-tooltip>
            <template slot="title">
              {{ commodityData.name }}
            </template>
            {{ commodityData.name.slice(0, 7) }}...
          </a-tooltip>
        </a-col>
        <a-col :span="8"><b>商品类型：</b>
          <span v-if="commodityData.type == 1">上装</span>
          <span v-if="commodityData.type == 2">下装</span>
          <span v-if="commodityData.type == 3">首饰</span>
          <span v-if="commodityData.type == 4">鞋子</span>
          <span v-if="commodityData.type == 5">内衣</span>
          <span v-if="commodityData.type == 6">化妆品</span>
        </a-col>
      </a-row>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col :span="8"><b>商品价格：</b>
          ￥{{ commodityData.price }}
        </a-col>
        <a-col :span="8"><b>上架状态：</b>
          <a-tag v-if="commodityData.onPut == 0" color="red">下架中</a-tag>
          <a-tag v-if="commodityData.onPut == 1" color="green">上架中</a-tag>
        </a-col>
        <a-col :span="8"><b>发布时间：</b>
          {{ commodityData.createDate }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">商品介绍</span></a-col>
        <a-col :span="24">
          {{ commodityData.content !== null ? commodityData.content : '- -' }}
        </a-col>
        <br/>
        <br/>
        <br/>
        <a-col :span="24">
          <a-upload
            name="avatar"
            action="http://127.0.0.1:9527/file/fileUpload/"
            list-type="picture-card"
            disabled
            :file-list="fileList"
            @preview="handlePreview"
            @change="picHandleChange"
          >
<!--            <div v-if="fileList.length < 8">-->
<!--              <a-icon type="plus" />-->
<!--              <div class="ant-upload-text">-->
<!--                Upload-->
<!--              </div>-->
<!--            </div>-->
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">订单评价</span></a-col>
        <a-col :span="24">
          <a-list
            class="comment-list"
            :header="`${replyList.length} 回复`"
            item-layout="horizontal"
            :data-source="replyList"
          >
            <a-list-item slot="renderItem" slot-scope="item, index">
              <a-comment :author="item.author" :avatar="item.avatar">
                <template slot="actions">
                  <span v-for="(action, index) in item.actions" :key="index">{{ action }}</span>
                </template>
                <p slot="content" style="margin-left: -40px">
                  {{ item.content }}
                </p>
                <a-tooltip slot="datetime" :title="item.datetime.format('YYYY-MM-DD HH:mm:ss')">
                  <span>{{ item.datetime.fromNow() }}</span>
                </a-tooltip>
              </a-comment>
            </a-list-item>
          </a-list>
        </a-col>
      </a-row>
      <br/>
      <br/>
    </div>
  </a-modal>
</template>

<script>
import moment from 'moment'
moment.locale('zh-cn')
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'CommodityView',
  props: {
    commodityShow: {
      type: Boolean,
      default: false
    },
    commodityData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.commodityShow
      },
      set: function () {
      }
    }
  },
  data () {
    return {
      loading: false,
      fileList: [],
      previewVisible: false,
      previewImage: '',
      replyList: []
    }
  },
  watch: {
    commodityShow: function (value) {
      if (value && this.commodityData.images !== null && this.commodityData.images !== '') {
        this.imagesInit(this.commodityData.images)
      }
      this.dataInit()
    }
  },
  methods: {
    dataInit () {
      this.$get('/cos/evaluation/getEvaluationByCommodityId', {id: this.commodityData.id}).then((r) => {
        let replyList = []
        r.data.data.forEach(item => {
          replyList.push({author: item.userName, avatar: item.avatar, content: item.content, datetime: moment(item.createDate)})
        })
        this.replyList = replyList
      })
    },
    handleCancel () {
      this.previewVisible = false
    },
    async handlePreview (file) {
      if (!file.url && !file.preview) {
        file.preview = await getBase64(file.originFileObj)
      }
      this.previewImage = file.url || file.preview
      this.previewVisible = true
    },
    picHandleChange ({ fileList }) {
      this.fileList = fileList
    },
    imagesInit (images) {
      if (images !== null && images !== '') {
        let imageList = []
        images.split(',').forEach((image, index) => {
          imageList.push({uid: index, name: image, status: 'done', url: 'http://127.0.0.1:9527/imagesWeb/' + image})
        })
        this.fileList = imageList
      }
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
