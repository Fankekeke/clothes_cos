const app = getApp();
let http = require('../../../utils/request')
import Notify from '../../../dist/notify/notify';
Page({
	data: {
		StatusBar: app.globalData.StatusBar,
		CustomBar: app.globalData.CustomBar,
		TabbarBot: app.globalData.tabbar_bottom,
		hidden: true,
    tag: '',
    content: '',
    fileList: [],
    auditInfo: null
	},
	onLoad: function (option) {
    this.isLogin()
	},
  isLogin() {
    wx.getStorage({
      key: 'userInfo',
      success: (res) => {
        this.getAuditInfo(res.data.id)
      },
      fail: res => {
        wx.showToast({
          title: '请先进行登录',
          icon: 'none',
          duration: 2000
        })
        setTimeout(() => {
          wx.switchTab({
            url: '../index/index'
          })
        }, 1500)
      }
    })
  },
  getAuditInfo(userId) {
    http.get('auditInfoByUser', {userId}).then((r) => {
      console.log(JSON.stringify(r.data))
      if (r.data !== null) {
        this.setData({ auditInfo: r.data })
      }
    })
  },
  submit() {
    wx.getStorage({
      key: 'userInfo',
      success: (res) => {
        if (this.data.tag == '' || this.data.content == '' || this.data.fileList === 0) {
          wx.showToast({
            title: '请完整填写！',
            icon: 'error',
            duration: 2000
          })
        } else {
          let images = []
          this.data.fileList.forEach(item => {
            images.push(item.url)
          });
          let data = {tag: this.data.tag, introduction: this.data.content, userId: res.data.id, images: images.length !== 0 ? images.join(',') : null}
          http.post('userAuditAdd', data).then((r) => {
            wx.showToast({
              title: '提交成功！',
              icon: 'success',
              duration: 2000
            })
            setTimeout(() => {
              wx.switchTab({
                url: '../index/index'
              })
            }, 1500)
          })
        }
      },
      fail: res => {
        wx.showToast({
          title: '请先进行登录',
          icon: 'none',
          duration: 2000
        })
      }
    })
  },
  afterRead(event) {
    const { file } = event.detail;
    let that = this
    // 当设置 mutiple 为 true 时, file 为数组格式，否则为对象格式
    wx.uploadFile({
      url: 'http://127.0.0.1:9527/file/fileUpload', // 仅为示例，非真实的接口地址
      filePath: file.url,
      name: 'avatar',
      success(res) {
        // 上传完成需要更新 fileList
        const { fileList = [] } = that.data;
        fileList.push({ ...file, url: res.data });
        that.setData({ fileList });
        console.log(JSON.stringify(fileList))
      },
    });
  }
});
