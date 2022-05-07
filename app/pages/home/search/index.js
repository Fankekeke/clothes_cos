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
        commodity: [],
        shop: []
    },
    onLoad: function (options) {
        this.setData({ key: options.key })
        this.getGoodsFuzzy()
    },
    getGoodsFuzzy() {
        http.get('getGoodsFuzzy', { key: this.data.key }).then((r) => {
            r.commodity.forEach(item => {
                item.image = item.images.split(',')[0]
            });
            this.setData({
                commodity: r.commodity,
                shop: r.shop
            })
		})
    },
    search() {
        this.getGoodsFuzzy()
    },
    getKeyValue(e) {
		this.setData({ key: e.detail.value })
	},
    tabSelect(e) {
        console.log(e.currentTarget.dataset.id);
        this.setData({
            TabCur: e.currentTarget.dataset.id,
            scrollLeft: (e.currentTarget.dataset.id-1)*60
        })
    }
});
