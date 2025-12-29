import { Outlet } from 'react-router-dom';
import Navbar from '../components/Navbar/Navbar';
import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useRef } from 'react';
import { getUserThunk } from '../app/features/users/usersThunk.js';
import { getTimeThunk } from '../app/features/time/timeThunk.js';
import { tick } from '../app/features/time/timeSlice.js';

const Layout = () => {
	const status = useSelector((state) => state.users.status);

	const dispatch = useDispatch();
	useEffect(() => {
		if (status === 'inactive') dispatch(getUserThunk());
	}, [status, dispatch]);

	useEffect(() => {
		if (status === 'inactive') {
			dispatch(getTimeThunk());
		}
	}, [dispatch, status]);

	// real-time ticking: dispatch tick every second while serverTime exists
	const timerRef = useRef(null);
	const serverTime = useSelector((state) => state.time.serverTime);

	useEffect(() => {
		if (serverTime) {
			// start ticking if not already started
			if (!timerRef.current) {
				timerRef.current = setInterval(() => dispatch(tick()), 1000);
			}
		} else {
			// stop ticking when no serverTime
			if (timerRef.current) {
				clearInterval(timerRef.current);
				timerRef.current = null;
			}
		}

		return () => {
			if (timerRef.current) {
				clearInterval(timerRef.current);
				timerRef.current = null;
			}
		};
	}, [serverTime, dispatch]);

	let navData = [
		{ to: '/', text: 'НОВОСТИ' },
		{ to: '/auth', text: 'ВОЙТИ В АККАУНТ' },
	];

	if (localStorage.getItem('token')) {
		navData = [
			{ to: '/', text: 'НОВОСТИ' },
			{ to: '/categories', text: 'КАТАЛОГ' },
			{ to: '/profile', text: 'ПРОФИЛЬ' },
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
