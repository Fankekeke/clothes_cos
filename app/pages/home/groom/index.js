const app = getApp();
let http = require('../../../utils/request')
Page({
	data: {
		StatusBar: app.globalData.StatusBar,
		CustomBar: app.globalData.CustomBar,
		hidden: true,
		shopList: []
	},
	onLoad: function (option) {
		this.getShopList()
	},
	shopDeatil(e) {
		wx.navigateTo({
			url: '/pages/shop/index/index?shopId='+e.currentTarget.dataset.shopid+''
		});
	},
	getShopList() {
		http.get('selShopDetailList').then((r) => {
			r.data.forEach(element => {
				if (element.goods) {
					element.goods.forEach(item => {
						item.image = item.images.split(',')[0]
					});
				}
			});
            this.setData({
                shopList: r.data
            })
        })
	}
});
