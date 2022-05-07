const app = getApp();
let http = require('../../../utils/request')
Page({
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    TabbarBot: app.globalData.tabbar_bottom,
    TabCur: 1, scrollLeft: 0,
    userInfo: null,
    otherName: '',
    replyInfo: [],
    content: '',
    takeUser: null,
    sendUser: null
  },
  onLoad: function (options) {
    wx.getStorage({
      key: 'userInfo',
      success: (res) => {
        this.setData({
          userInfo: res.data,
          otherName: options.otherName,
          takeUser: options.takeUser,
          sendUser: options.sendUser
        })
        this.getMessageInfo(this.data.takeUser, this.data.sendUser)
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
  getContent(e) {
    this.setData({ content: e.detail.value })
  },
  reply() {
    if (this.data.content == '') {
      wx.showToast({
        title: '请输入回复内容',
        icon: 'none',
        duration: 2000
      })
    } else {
      wx.getStorage({
        key: 'userInfo',
        success: (res) => {
          let data = {takeUser: this.data.sendUser == res.data.id ? this.data.takeUser : this.data.sendUser, sendUser: res.data.id, content: this.data.content}
          http.post('messageReply', data).then((r) => {
            this.setData({content: ''})
            this.getMessageInfo(this.data.takeUser, this.data.sendUser)
          })
        },
        fail: res => {
          wx.showToast({
            title: '请先进行登录',
            icon: 'none',
            duration: 2000
          })
        }
      })
    }
  },
  getMessageInfo(takeUser, sendUser) {
    http.get('getMessageDetail', { takeUser, sendUser, userId: this.data.userInfo.id }).then((r) => {
      this.setData({ replyInfo: r.data })
    })
  }
});
