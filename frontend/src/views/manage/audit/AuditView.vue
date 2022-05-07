<template>
  <a-modal v-model="show" title="设计师审核" @cancel="onClose" :width="800">
    <template slot="footer">
      <a-button key="back" @click="check(2)" type="danger">
        驳回
      </a-button>
      <a-button key="check" @click="check(1)">
        通过
      </a-button>
    </template>
    <div style="font-size: 13px" v-if="auditData !== null">
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">基础信息</span></a-col>
        <a-col :span="8"><b>申请人：</b>
          {{ auditData.userName }}
        </a-col>
        <a-col :span="8"><b>申请时间：</b>
          {{ auditData.createDate }}
        </a-col>
        <a-col :span="8"><b>审核状态：</b>
          <a-tag v-if="auditData.auditStatus == 0">未审核</a-tag>
          <a-tag v-if="auditData.auditStatus == 1" color="green">审核通过</a-tag>
          <a-tag v-if="auditData.auditStatus == 2" color="red">驳回</a-tag>
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">标签</span></a-col>
        <a-col :span="24">
          <a-tag v-if="auditData.tag !== null" v-for="(item, index) in auditData.tag.split(',')" :key="index">{{ item }}</a-tag>
          <span v-else>暂无标签</span>
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">简介</span></a-col>
        <a-col :span="24">
          {{ auditData.introduction !== null ? auditData.introduction : '- -' }}
        </a-col>
      </a-row>
      <br/>
      <br/>
      <a-row style="padding-left: 24px;padding-right: 24px;">
        <a-col style="margin-bottom: 15px"><span style="font-size: 15px;font-weight: 650;color: #000c17">证明文件</span></a-col>
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
            <div v-if="fileList.length < 8">
              <a-icon type="plus" />
              <div class="ant-upload-text">
                Upload
              </div>
            </div>
          </a-upload>
          <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
            <img alt="example" style="width: 100%" :src="previewImage" />
          </a-modal>
        </a-col>
      </a-row>
      <br/>
      <br/>
    </div>
  </a-modal>
</template>

<script>
function getBase64 (file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => resolve(reader.result)
    reader.onerror = error => reject(error)
  })
}
export default {
  name: 'AuditView',
  props: {
    auditShow: {
      type: Boolean,
      default: false
    },
    auditData: {
      type: Object
    }
  },
  computed: {
    show: {
      get: function () {
        return this.auditShow
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
      previewImage: ''
    }
  },
  watch: {
    auditShow: function (value) {
      if (value && this.auditData.images !== null && this.auditData.images !== '') {
        this.imagesInit(this.auditData.images)
      }
    }
  },
  methods: {
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
    check (type) {
      this.$put('/cos/audit-info/check', {auditId: this.auditData.id, type: type}).then((r) => {
        this.$emit('checkClose')
      })
    },
    onClose () {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
