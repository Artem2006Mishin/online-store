import { createBrowserRouter } from 'react-router-dom';

import Layout from './layout/Main/Layout';
import NewsPage from './pages/NewsPage';
import CatalogPage from './pages/CatalogPage';
import CatalogLayout from './layout/Catalog/CatalogLayout';

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
				element: <CatalogLayout />,
				children: [
					{
						index: true,
						element: <CatalogPage />,
					},
					{
						path: ':slug-:id',
						element: <div>продукты</div>,
					},
				],
			},
		],
	},
]);

// TODO: добавить 404
