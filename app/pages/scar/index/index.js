const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        orderList: [],
        userInfo: null,
        allPrice: 0,
        allChecked: true
    },
    onLoad: function (options) {

    },
    onShow() {
        console.log(111)
        this.isLogin()
    },
    isLogin() {
        wx.getStorage({
            key: 'userInfo',
            success: (res) => {
                this.setData({
                    userInfo: res.data
                })
                this.selGoodsCart(res.data.id)
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
    selGoodsCart(userId) {
        http.get('selGoodsCart', { userId }).then((r) => {
            let allPrice = 0
            r.data.forEach(item => {
                allPrice += item.price
                item.checked = true
                item.image = item.images.split(',')[0],
                    item.days = this.timeFormat(item.createDate)
            });
            this.setData({ orderList: r.data, allPrice })
        })
    },
    checkAll(e) {
        let orderList = this.data.orderList
        if (this.data.allChecked) {
            orderList.forEach(item => {
                item.checked = false
            });
            this.setData({
                allPrice: 0,
                orderList,
                allChecked: false
            })
        } else {
            let allPrice = 0
            orderList.forEach(item => {
                item.checked = true
                allPrice += item.price
            });
            this.setData({
                allPrice,
                orderList,
                allChecked: true
            })
        }
    },
    checkboxChange(e) {
        let orderList = this.data.orderList
        orderList.forEach(item => {
            item.checked = false
        });
        if (e.detail.value.length == 0) {
            this.setData({ allPrice: 0, orderList })
        } else {
            let allPrice = 0
            orderList.forEach(item => {
                e.detail.value.forEach(element => {
                    if (element == item.orderId) {
                        item.checked = true
                        allPrice += item.price
                    }
                });
            });
            this.setData({ allPrice: allPrice, orderList })
        }
    },
    submit: function () {
        let orderIds = []
        this.data.orderList.forEach(item => {
            if (item.checked) {
                orderIds.push(item.orderId)
            }
        });
        if (orderIds.length == 0) {
            wx.showToast({
                title: '请选择订单',
                icon: 'none',
                duration: 2000
            })
        } else {
            http.get('selDefaultAddress', { userId: this.data.userInfo.id }).then((r) => {
                if (r.data == null) {
                    wx.showToast({
                        title: '请先设置默认收货地址',
                        icon: 'none',
                        duration: 1000
                    })
                } else {
                    let orderIds = []
                    this.data.orderList.forEach(item => {
                        if (item.checked) {
                            orderIds.push(item.orderId)
                        }
                    });
                    wx.navigateTo({
                        url: '/pages/scar/order/index?type=1&orderIds=' + orderIds.join(',') + ''
                    })
                }
            })
        }
    }
});
