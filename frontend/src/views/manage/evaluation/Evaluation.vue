<template>
  <a-card :bevaluationed="false" :bordered="false"  class="card-area">
    <div :class="advanced ? 'search' : null">
      <!-- 搜索区域 -->
      <a-form layout="horizontal">
        <a-row :gutter="15">
          <div :class="advanced ? null: 'fold'">
            <a-col :md="6" :sm="24">
              <a-form-item
                label='商品名称'
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.name"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="店铺名"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-input v-model="queryParams.userName"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="商品类型"
                :labelCol="{span: 4}"
                :wrapperCol="{span: 18, offset: 2}">
                <a-select v-model="queryParams.type" allowClear>
                  <a-select-option value="1">上装</a-select-option>
                  <a-select-option value="2">下装</a-select-option>
                  <a-select-option value="3">首饰</a-select-option>
                  <a-select-option value="4">鞋子</a-select-option>
                  <a-select-option value="5">内衣</a-select-option>
                  <a-select-option value="6">化妆品</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </div>
          <span style="float: right; margin-top: 3px;">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <!--        <a-button type="primary" ghost @click="add">新增</a-button>-->
        <a-button @click="batchDelete">删除</a-button>
      </div>
      <!-- 表格区域 -->
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
               :scroll="{ x: 900 }"
               @change="handleTableChange">
        <template slot="nameShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.name }}
              </template>
              {{ record.name.slice(0, 10) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="userNameShow" slot-scope="text, record">
          <a-popover>
            <template slot="content">
              <a-avatar shape="square" size={132} icon="user" :src="record.avatar" />
            </template>
            <a>{{ record.userName }}</a>
          </a-popover>
        </template>
        <template slot="evaluationContentShow" slot-scope="text, record">
          <template>
            <a-tooltip>
              <template slot="title">
                {{ record.evaluationContent }}
              </template>
              {{ record.evaluationContent.slice(0, 15) }} ...
            </a-tooltip>
          </template>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-icon type="reconciliation" @click="view(record)" title="查 看"></a-icon>
        </template>
      </a-table>
    </div>
    <evaluation-view
      @close="handleEvaluationViewClose"
      :evaluationShow="evaluationView.visiable"
      :evaluationData="evaluationView.data">
    </evaluation-view>
  </a-card>
</template>

<script>
import RangeDate from '@/components/datetime/RangeDate'
import {mapState} from 'vuex'
import moment from 'moment'
import EvaluationView from './EvaluationView'
moment.locale('zh-cn')

export default {
  name: 'evaluation',
  components: {EvaluationView, RangeDate},
  data () {
    return {
      evaluationView: {
        visiable: false,
        data: null
      },
      advanced: false,
      queryParams: {},
      filteredInfo: null,
      sortedInfo: null,
      paginationInfo: null,
      dataSource: [],
      selectedRowKeys: [],
      loading: false,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      userList: []
    }
  },
  computed: {
    ...mapState({
      currentUser: state => state.account.user
    }),
    columns () {
      return [{
        title: '订单编号',
        dataIndex: 'code'
      }, {
        title: '商品名称',
        dataIndex: 'name',
        scopedSlots: { customRender: 'nameShow' }
      }, {
        title: '商品类型',
        dataIndex: 'type',
        customRender: (text, row, index) => {
          switch (text) {
            case 1:
              return <a-tag>上装</a-tag>
            case 2:
              return <a-tag>下装</a-tag>
            case 3:
              return <a-tag>首饰</a-tag>
            case 4:
              return <a-tag>鞋子</a-tag>
            case 5:
              return <a-tag>内衣</a-tag>
            case 6:
              return <a-tag>化妆品</a-tag>
            default:
              return '- -'
          }
        }
      }, {
        title: '订单价格',
        dataIndex: 'price',
        customRender: (text, row, index) => {
          if (text !== null) {
            return '￥' + text
          } else {
            return '- -'
          }
        }
      }, {
        title: '评价人',
        dataIndex: 'userName',
        scopedSlots: { customRender: 'userNameShow' }
      }, {
        title: '分数',
        dataIndex: 'score'
      }, {
        title: '评价内容',
        dataIndex: 'evaluationContent',
        scopedSlots: { customRender: 'evaluationContentShow' }
      }, {
        title: '评价时间',
        dataIndex: 'createDate',
        customRender: (text, row, index) => {
          if (text !== null) {
            return text
          } else {
            return '- -'
          }
        }
      }, {
        title: '操作',
        dataIndex: 'operation',
        scopedSlots: {customRender: 'operation'}
      }]
    }
  },
  mounted () {
    this.fetch()
  },
  methods: {
    view (row) {
      this.evaluationView.data = row
      this.evaluationView.visiable = true
    },
    handleEvaluationViewClose () {
      this.evaluationView.visiable = false
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    handleDeptChange (value) {
      this.queryParams.deptId = value || ''
    },
    batchDelete () {
      if (!this.selectedRowKeys.length) {
        this.$message.warning('请选择需要删除的记录')
        return
      }
      let that = this
      this.$confirm({
        title: '确定删除所选中的记录?',
        content: '当您点击确定按钮后，这些记录将会被彻底删除',
        centered: true,
        onOk () {
          let ids = that.selectedRowKeys.join(',')
          that.$delete('/cos/evaluation/' + ids).then(() => {
            that.$message.success('删除成功')
            that.selectedRowKeys = []
            that.search()
          })
        },
        onCancel () {
          that.selectedRowKeys = []
        }
      })
    },
    search () {
      let {sortedInfo, filteredInfo} = this
      let sortField, sortevaluation
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field
        sortevaluation = sortedInfo.evaluation
      }
      this.fetch({
        sortField: sortField,
        sortevaluation: sortevaluation,
        ...this.queryParams,
        ...filteredInfo
      })
    },
    reset () {
      // 取消选中
      this.selectedRowKeys = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent
        this.paginationInfo.pageSize = this.pagination.defaultPageSize
      }
      // 重置列过滤器规则
      this.filteredInfo = null
      // 重置列排序规则
      this.sortedInfo = null
      // 重置查询参数
      this.queryParams = {}
      this.fetch()
    },
    handleTableChange (pagination, filters, sorter) {
      // 将这三个参数赋值给Vue data，用于后续使用
      this.paginationInfo = pagination
      this.filteredInfo = filters
      this.sortedInfo = sorter

      this.fetch({
        sortField: sorter.field,
        sortevaluation: sorter.evaluation,
        ...this.queryParams,
        ...filters
      })
    },
    fetch (params = {}) {
      // 显示loading
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.size = this.paginationInfo.pageSize
        params.current = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.size = this.pagination.defaultPageSize
        params.current = this.pagination.defaultCurrent
      }
      if (params.type === undefined) {
        delete params.type
      }
      this.$get('/cos/evaluation/page', {
        ...params
      }).then((r) => {
        let data = r.data.data
        const pagination = {...this.pagination}
        pagination.total = data.total
        this.dataSource = data.records
        this.pagination = pagination
        // 数据加载完毕，关闭loading
        this.loading = false
      })
    }
  },
  watch: {}
}
</script>
<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
