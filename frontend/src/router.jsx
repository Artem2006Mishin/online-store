import { createBrowserRouter } from 'react-router-dom';

import Layout from './layout/Layout';
import NewsPage from './pages/NewsPage/NewsPage';
import CatalogPage from './pages/CatalogPage/CatalogPage';

export const router = createBrowserRouter([
	{
		path: '/',
		element: <Layout />,
		children: [
			{
				index: true,
				element: <NewsPage />,
			},
			{
				path: 'catalog',
				element: <CatalogPage />,
				children: [
					{
						path: 'computers',
						element: <div>пк</div>,
					},
					{
						path: 'smartphones',
						element: <div>телефоны</div>,
					},
					{
						path: 'technic',
						element: <div>прочая техника</div>,
					},
				],
			},
		],
	},
]);

// TODO: добавить 404
