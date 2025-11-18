import { createSlice } from '@reduxjs/toolkit';

const initialState = {
	items: [
		{
			link: '/',
			text: 'НОВОСТИ',
		},
		{
			link: '/catalog',
			text: 'КАТАЛОГ',
		},
	],
};

const navbarSlice = createSlice({
	name: 'navbar',
	initialState,
	reducers: {
		switchingNews(state) {
			state.items = [
				{
					link: '/',
					text: 'НОВОСТИ',
				},
				{
					link: '/catalog',
					text: 'КАТАЛОГ',
				},
			];
		},

		switchingCatalog(state) {
			state.items = [
				{
					link: '/',
					text: 'НОВОСТИ',
				},
				{
					link: '/catalog',
					text: 'КАТАЛОГ',
				},
				{
					link: '/catalog/computers',
					text: 'КОМПЬЮТЕРЫ',
				},
				{
					link: '/catalog/smartphones',
					text: 'СМАРТФОНЫ',
				},
				{
					link: '/catalog/technic',
					text: 'ТЕХНИКА ДЛЯ ДОМА',
				},
			];
		},
	},
});

export const { switchingNews, switchingCatalog } = navbarSlice.actions;
export default navbarSlice.reducer;
