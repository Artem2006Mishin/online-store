import { createBrowserRouter } from 'react-router-dom';

import Layout from './layout/Layout';
import NewsPage from './pages/NewsPage';
import CatalogPage from './pages/CatalogPage';
import CatalogLayout from './layout/CatalogLayout';
import AuthenticationLayout from './layout/AuthenticationLayout';
import RegistrationPage from './pages/RegistrationPage';
import LoginPage from './pages/LoginPage';

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
			{
				path: 'authentication',
				element: <AuthenticationLayout />,
				children: [
					{
						index: true,
						element: <LoginPage />,
					},
					{
						path: 'registration',
						element: <RegistrationPage />,
					},
				],
			},
		],
	},
]);

// TODO: добавить 404
