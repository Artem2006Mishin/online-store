import { createBrowserRouter } from 'react-router-dom';

import Layout from './layout/Layout';
import NewsPage from '../pages/NewsPage';
import CatalogPage from '../pages/CatalogPage';

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
			},
		],
	},
]);

// TODO: добавить errorElement
// TODO: заменить element: на lazy-loading
