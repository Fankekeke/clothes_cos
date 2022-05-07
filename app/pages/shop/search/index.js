const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar,
        CustomBar: app.globalData.CustomBar,
        TabbarBot: app.globalData.tabbar_bottom,
        TabCur: 0,scrollLeft:0,
        SortMenu: [{id:0,name:"商品列表"},{id:1,name:"店铺列表"}],
        key: '',
        shopId: null,
        goodsList: []
    },
    onLoad: function (options) {
        this.setData({
            key: options.key,
            shopId: options.shopId
        })
        this.search()
    },
    getKeyValue(e) {
		this.setData({ key: e.detail.value })
	},
    goodsDetail(e) {
        wx.navigateTo({
			url: '/pages/shop/goods/details?commoditId='+e.currentTarget.dataset.commoditid+''
		});
    },
    search() {
        http.get('commodityLikeByShop', {shopId: this.data.shopId, key: this.data.key}).then((r) => {
			r.data.forEach(item => {
                item.image = item.images.split(',')[0]
			});
            this.setData({
                goodsList: r.data
            })
        })
    },
    tabSelect(e) {
        console.log(e.currentTarget.dataset.id);
        this.setData({
            TabCur: e.currentTarget.dataset.id,
            scrollLeft: (e.currentTarget.dataset.id-1)*60
        })
    }
});
