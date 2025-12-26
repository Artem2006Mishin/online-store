import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar/Navbar';
import {useState} from "react";

const Layout = () => {
	const token = localStorage.getItem('token');

  const [navData, setNavData] = useState([
    { to: '/', text: 'НОВОСТИ' },
    { to: '/auth', text: 'ВОЙТИ В АККАУНТ' },
  ]);

  // if (token) {
  //   setNavData([
  //     ...navData,
  //     { to: '/categories', text: 'КАТАЛОГ' },
  //   ]);
  // }

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
