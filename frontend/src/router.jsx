import { createBrowserRouter } from 'react-router-dom';

import Layout from './layout/Layout';
import NewsPage from './pages/NewsPage';
import CatalogPage from './pages/CatalogPage';
import CatalogLayout from './layout/CatalogLayout';
import RegistrationPage from './pages/RegistrationPage';
import LoginPage from './pages/LoginPage';
import AuthLayout from './layout/AuthLayout';
import ProfilePage from './pages/ProfilePage';

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
				path: 'auth',
				element: <AuthLayout />,
				children: [
					{
						index: true,
						element: <LoginPage />,
					},
					{
						path: 'reg',
						element: <RegistrationPage />,
					},
				],
			},
			{
				path: 'profile',
				element: <ProfilePage />,
			},
		],
	},
]);

// TODO: добавить 404
// TODO: заменить на @RequestMapping("/auth")
