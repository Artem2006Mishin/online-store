import { createBrowserRouter } from 'react-router-dom';

import Layout from './layout/Layout';
import NewsPage from './pages/NewsPage/NewsPage';
import CatalogPage from './pages/CatalogPage';
import ErrorPage from './pages/ErrorPage';

export const router = createBrowserRouter([
	{
		path: '/',
		element: <Layout />,
		children: [
			{
				index: true,
				element: <NewsPage />,
				errorElement: <ErrorPage />,
			},
			{
				path: 'catalog',
				element: <CatalogPage />,
			},
		],
	},
]);

// TODO: заменить element: на lazy-loading
