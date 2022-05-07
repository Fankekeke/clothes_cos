const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        TabbarBot: app.globalData.tabbar_bottom,
        swiperlist: [
            'https://image.weilanwl.com/img/4x3-1.jpg',
            'https://image.weilanwl.com/img/4x3-2.jpg',
            'https://image.weilanwl.com/img/4x3-3.jpg',
            'https://image.weilanwl.com/img/4x3-4.jpg',
        ],
        shopInfo: null
    },
    onLoad: function (options) {
        this.selShopDetail(options.shopId)
    },
    selShopDetail(shopId) {
        http.get('getShopDetail', {shopId}).then((r) => {
            this.setData({
                shopInfo: r
            })
        })
    },
});
