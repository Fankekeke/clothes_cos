const app = getApp();
let http = require('../../../utils/request')
import Notify from '../../../dist/notify/notify';
Page({
	data: {
		StatusBar: app.globalData.StatusBar,
		CustomBar: app.globalData.CustomBar,
		TabbarBot: app.globalData.tabbar_bottom,
		hidden: true,
		addressList: [],
		userInfo: null,
		arraylist: [{
			id: 1,
			name: "名字",
			phone: "13800138000",
			address: "重庆市某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某",
			checked: false
		}, {
			id: 2,
			name: "名字2",
			phone: "13800138000",
			address: "重庆市某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某某",
			checked: true
		}]
	},
	onLoad: function (option) {
		this.isLogin()
	},
	isLogin() {
		wx.getStorage({
			key: 'userInfo',
			success: (res) => {
				this.setData({userInfo: res.data})
				this.getAddressInfo(res.data.id)
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
	getAddressInfo(userId) {
		http.get('addressInfoByUser', { userId }).then((r) => {
			if (r.data !== null) {
				this.setData({ addressList: r.data })
			}
		})
	},
	addressAdd(address) {
		http.post('addressAdd', address).then((r) => {
			wx.showToast({
				title: '添加成功',
				icon: 'success',
				duration: 2000
			})
			this.getAddressInfo(this.data.userInfo.id)
		})
	},
	addressEdit(address) {
		http.post('addressEdit', address).then((r) => {
			wx.showToast({
				title: '修改成功',
				icon: 'success',
				duration: 2000
			})
		})
	},
	edit(event) {
		wx.redirectTo({
			url: '/pages/address/edit/index?addressId='+event.currentTarget.dataset.addressid+''
		});
	},
	add: function () {
		let that = this;
		wx.showModal({
			title: '提示',
			content: '是否获取微信的收货地址？',
			success(res) {
				if (res.confirm) {
					that.wxaddress();
				} else if (res.cancel) {
					wx.redirectTo({
						url: '/pages/address/add/index'
					});
				}
			}
		});
	},
	//获取微信的收货地址
	wxaddress: function () {
		let that = this;
		wx.getSetting({
			success: res => {
				if (res.authSetting['scope.address']) {
					wx.chooseAddress({
						success(res) {
							console.log(res)
							let address = res.provinceName + '' + res.cityName + '' + res.countyName + '' + res.detailInfo
							let data = { address, name: res.userName, phone: res.telNumber, userId: that.data.userInfo.id, defaultAddress: 0 }
							that.addressAdd(data)
						}
					})
				}
			}
		});
	}
});
