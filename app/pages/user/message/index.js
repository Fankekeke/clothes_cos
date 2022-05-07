const app = getApp();
let http = require('../../../utils/request')
Page({
  data: {
    StatusBar: app.globalData.StatusBar,
    CustomBar: app.globalData.CustomBar,
    userInfo: null,
    messageList: []
  },
  onLoad: function (options) {

  },
  onShow() {
    this.isLogin()
  },
  /**
* 页面相关事件处理函数--监听用户下拉动作
*/
  onPullDownRefresh() {
    this.isLogin()
  },
  isLogin() {
    wx.getStorage({
      key: 'userInfo',
      success: (res) => {
        this.setData({ userInfo: res.data })
        this.messageList()
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
  messageDetail(event) {
    let takeUser = event.currentTarget.dataset.take
    let sendUser = event.currentTarget.dataset.send
    let takeUserName = event.currentTarget.dataset.takename
    let sendUserName = event.currentTarget.dataset.sendname
    let otherName = sendUser == this.data.userInfo.id ? takeUserName : sendUserName
    wx.navigateTo({
			url: '/pages/user/detail/index?takeUser='+takeUser+'&sendUser='+sendUser+'&otherName='+otherName+''
		});
  },
  messageList() {
    http.get('messageListById', { userId: this.data.userInfo.id }).then((r) => {
      r.data.forEach(item => {
        item.days = this.timeFormat(item.createDate)
      });
      this.setData({ messageList: r.data })
    })
  },
  timeFormat(time) {
    var nowTime = new Date();
    var day = nowTime.getDate();
    var hours = parseInt(nowTime.getHours());
    var minutes = nowTime.getMinutes();
    // 开始分解付入的时间
    var timeday = time.substring(8, 10);
    var timehours = parseInt(time.substring(11, 13));
    var timeminutes = time.substring(14, 16);
    var d_day = Math.abs(day - timeday);
    var d_hours = hours - timehours;
    var d_minutes = Math.abs(minutes - timeminutes);
    if (d_day <= 1) {
      switch (d_day) {
        case 0:
          if (d_hours == 0 && d_minutes > 0) {
            return d_minutes + '分钟前';
          } else if (d_hours == 0 && d_minutes == 0) {
            return '1分钟前';
          } else {
            return d_hours + '小时前';
          }
          break;
        case 1:
          if (d_hours < 0) {
            return (24 + d_hours) + '小时前';
          } else {
            return d_day + '天前';
          }
          break;
      }
    } else if (d_day > 1 && d_day < 10) {
      return d_day + '天前';
    } else {
      return time;
    }
  },
  submit: function () {
    wx.navigateTo({
      url: '/pages/scar/order/index'
    })
  }
});
