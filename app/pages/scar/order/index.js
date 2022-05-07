const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        TabbarBot: app.globalData.tabbar_bottom,
        orderType: 0,
        orderIds: [],
        addressInfo: null,
        commodityId: null,
        orderList: [],
        allPrice: 0,
        userInfo: null
    },
    onLoad: function (options) {
        
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                this.setData({
                    orderType: options.type,
                    userInfo: res.data
                })
                if (options.type == 1) {
                    this.setData({
                        orderIds: options.orderIds.split(',')
                    })
                    this.getOrderList(this.data.orderIds)
                } else {
                    this.setData({
                        commodityId: options.commodityId
                    })
                    this.getGoodsDetail(this.data.commodityId)
                }
                this.getUserAddress(res.data.id)
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
    getUserAddress(userId) {
        http.get('selDefaultAddress', { userId }).then((r) => {
            this.setData({ addressInfo: r.data })
        })
    },
    getGoodsDetail(commodityId) {
        http.get('goodsDetail', { commodityId }).then((r) => {
            let order = r
            let allPrice = order.price
            order.image = order.images.split(',')[0]
            this.setData({ orderList: [order], allPrice: allPrice.toFixed(2) })
        })
    },
    getOrderList(orderIds) {
        http.get('selOrderListByOrderIds', { ids: orderIds.join(',') }).then((r) => {
            let allPrice = 0
            r.data.forEach(item => {
                item.image = item.images.split(',')[0]
                allPrice += item.price
            });
            this.setData({ orderList: r.data, allPrice: allPrice.toFixed(2) })
        })
    },
    submit() {
        wx.showLoading({
            title: '正在模拟支付',
        })
        setTimeout(() => {
            if (this.data.orderType == 1) {
                let data = { ids: this.data.orderIds.join(',') }
                http.get('goodsCartComplete', data).then((r) => {
                    wx.showToast({
                        title: '支付成功',
                        icon: 'success',
                        duration: 1000
                    })
                    setTimeout(() => {
                        wx.navigateBack({ changed: true });
                    }, 1000)
                })
            } else {
                let data = { commodityId: this.data.commodityId, price: this.data.allPrice, addressId: this.data.addressInfo.id, userId: this.data.userInfo.id }
                http.post('buyGoods', data).then((r) => {
                    wx.showToast({
                        title: '支付成功',
                        icon: 'success',
                        duration: 1000
                    })
                    setTimeout(() => {
                        wx.navigateBack({ changed: true });
                    }, 1000)
                })
            }
            wx.hideLoading()
        }, 1000)
    }
});
