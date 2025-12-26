import {createBrowserRouter} from 'react-router-dom';

import Layout from './layout/Layout';
import NewsPage from './pages/NewsPage';
import CategoriesLayout from './layout/CategoriesLayout.jsx';
import CategoriesPage from './pages/CategoriesPage.jsx';
import ProductsPage from './pages/ProductsPage.jsx';
import AuthLayout from './layout/AuthLayout';
import LoginPage from './pages/LoginPage';
import RegistrationPage from './pages/RegistrationPage';
import ProfilePage from './pages/ProfilePage';
import OrderPage from "./pages/OrderPage.jsx";
import NotFoundPage from "./pages/NotFoundPage.jsx";

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout/>,
    children: [
      {
        index: true,
        element: <NewsPage/>,
      },
      {
        path: 'categories',
        element: <CategoriesLayout/>,
        children: [
          {
            index: true,
            element: <CategoriesPage/>,
          },
          {
            path: ':name',
            element: <ProductsPage/>,
          },
        ],
      },
      {
        path: 'auth',
        element: <AuthLayout/>,
        children: [
          {
            index: true,
            element: <LoginPage/>,
          },
          {
            path: 'reg',
            element: <RegistrationPage/>,
          },
        ],
      },
      {
        path: 'profile',
        element: <ProfilePage/>,
      },
      {
        path: 'order',
        element: <OrderPage/>,
      }
    ],
  },
  {
    path: '*',
    element: <NotFoundPage/>,
  }
]);

