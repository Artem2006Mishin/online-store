import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar/Navbar';
import {useDispatch, useSelector} from "react-redux";
import {useEffect} from "react";
import {getUserThunk} from "../app/features/users/usersThunk.js";
import {getTimeThunk} from "../app/features/time/timeThunk.js";

const Layout = () => {
  const status = useSelector(state => state.users.status);

  const dispatch = useDispatch();
  useEffect(() => {
    if (status === 'inactive') dispatch(getUserThunk());
  }, [status, dispatch]);

  useEffect(() => {
    if (status === 'inactive') {
      dispatch(getTimeThunk());
    }
  }, [dispatch, status]);

  let navData = [
    { to: '/', text: 'НОВОСТИ' },
    { to: '/auth', text: 'ВОЙТИ В АККАУНТ' },
  ];

  if (status === 'success') {
    navData = [
      {to: '/', text: 'НОВОСТИ'},
      {to: '/categories', text: 'КАТАЛОГ'},
      {to: '/profile', text: 'ПРОФИЛЬ'}
    ];
  }

	return (
		<>
			<header>
        <Navbar data={navData} />
			</header>

			<main>
				<Outlet />
			</main>
		</>
	);
};

export default Layout;



