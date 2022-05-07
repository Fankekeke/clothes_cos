const app = getApp();
let http = require('../../../utils/request')
Page({
	data: {
		StatusBar: app.globalData.StatusBar,
		CustomBar: app.globalData.CustomBar,
		TabbarBot: app.globalData.tabbar_bottom,
		hidden: true,
		region: ['重庆市', '重庆市', '江北区'],
		name: '',
		phone: '',
		address: '',
		defaultAddress: 1
	},
	onLoad: function (option) {
		// wx.getSetting({
		// 	success: res => {
		// 		if (!res.authSetting['scope.userInfo']) {
		// 			wx.redirectTo({
		// 				url: '/pages/auth/auth'
		// 			})
		// 		}
		// 	}
		// });
	},
	RegionChange: function (e) {
		this.setData({
			region: e.detail.value
		})
	},
	add() {
		wx.getStorage({
			key: 'userInfo',
			success: (res) => {
				let data = { name: this.data.name, phone: this.data.phone, address: this.data.address, defaultAddress: this.data.defaultAddress, userId: res.data.id }
				this.addressAdd(data)
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
	getNameValue(e) {
		this.setData({ name: e.detail.value })
	},
	getAddressValue(e) {
		this.setData({ address: e.detail.value })
	},
	getPhoneValue(e) {
		this.setData({ phone: e.detail.value })
	},
	addressAdd(address) {
		http.post('addressAdd', address).then((r) => {
			wx.showToast({
				title: '添加成功',
				icon: 'success',
				duration: 2000
			})
			setTimeout(() => {
				wx.redirectTo({
					url: '/pages/user/address/index'
				});
			}, 1000)
		})
	},
	switch1Change(event) {
		let defaultAddress = event.detail.value ? 1 : 0
		this.setData({ defaultAddress })
	}
});
