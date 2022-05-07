const app = getApp();
let http = require('../../../utils/request')
Page({
    data: {
        StatusBar: app.globalData.StatusBar + 6,
        TabbarBot: app.globalData.tabbar_bottom,
        TabCur: 0,scrollLeft:0,
        SortMenu: [{id:0,name:"综合"},{id:1,name:"销量"},{id:2,name:"新品"},{id:3,name:"价格"}],
        ShopList: [],
        shopId: null,
        shopInfo: null,
        key: ''
    },
    onLoad: function (options) {
        this.setData({
            shopId: options.shopId
        })
        this.selShopDetail(options.shopId)
    },
    selShopDetail(shopId) {
        http.get('getShopDetail', {shopId}).then((r) => {
            let ShopList = []
			r.goods.forEach(item => {
                ShopList.push({ index: item.id, image: item.images.split(',')[0], title: item.name, price: item.price, sales: item.orderNum })
			});
            this.setData({
                shopInfo: r,
                ShopList
            })
        })
    },
    commoditSort(shopId, type) {
        http.get('shopCommoditSort', {shopId, type}).then((r) => {
            let ShopList = []
			r.data.forEach(item => {
                ShopList.push({ index: item.id, image: item.images.split(',')[0], title: item.name, price: item.price, sales: item.orderNum })
			});
            this.setData({
                ShopList
            })
        })
    },
    tabSelect(e) {
        this.commoditSort(this.data.shopId, e.currentTarget.dataset.id)
        this.setData({
            TabCur: e.currentTarget.dataset.id,
            scrollLeft: (e.currentTarget.dataset.id-1)*60
        })
    },
    btnback: function () {
        wx.navigateBack();
    },
    getKeyValue(e) {
		this.setData({ key: e.detail.value })
	},
    search: function () {
        wx.navigateTo({
            url: '/pages/shop/search/index?key='+this.data.key+'&shopId='+this.data.shopId+''
        });
    }
});
