const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        TabbarBot: app.globalData.tabbar_bottom,
        orderInfo: null
    },
    onLoad: function (options) {
        this.getOrderDetail(options.orderId)
    },
    message(event) {
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                wx.navigateTo({
                    url: '/pages/user/detail/index?takeUser=' + event.currentTarget.dataset.shopid + '&sendUser=' + res.data.id + '&otherName=' + event.currentTarget.dataset.shopname + ''
                });
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
    takePhone() {
        wx.makePhoneCall({
            phoneNumber: '1340000'
          })
    },
    getOrderDetail(orderId) {
        http.get('selOrderListByOrderIds', {ids: orderId}).then((r) => {
            r.data.forEach(item => {
                item.image = item.images.split(',')[0]
            });
			this.setData({
                orderInfo: r.data[0]
            })
		})
    }
});
