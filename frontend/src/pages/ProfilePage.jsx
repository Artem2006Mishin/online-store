import Header from '../components/Header/Header';
import Section from '../components/Section/Section';
import Detail from '../components/Detail/Detail';
import { useEffect } from 'react';
import { getUserThunk } from '../app/features/users/usersThunk';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import { useDispatch, useSelector } from 'react-redux';
import { getTimeThunk } from '../app/features/time/timeThunk.js';
import { tick } from '../app/features/time/timeSlice.js';
import { resetCart } from '../app/features/users/usersSlice.js';
import { useNavigate } from 'react-router-dom';

const ProfilePage = () => {
	const { userData, status, error } = useSelector(state => state.users);
	const { serverTime } = useSelector(state => state.time);
	const dispatch = useDispatch();

	useEffect(() => {
		if (status === 'inactive') dispatch(getUserThunk());
	}, [status, dispatch]);

	useEffect(() => {
		if (status === 'inactive') {
			dispatch(getTimeThunk());
		}
	}, [dispatch, status]);

	useEffect(() => {
		if (!serverTime) return;

		const interval = setInterval(() => {
			dispatch(tick());
		}, 1000);

		return () => clearInterval(interval);
	}, [serverTime, dispatch]);

	const navigate = useNavigate();
	const handleClick = () => {
		localStorage.removeItem('token');
		dispatch(resetCart());
		navigate('/auth');
	};

	return (
		<Section>
			<Header title='Профиль' />

			{status === 'loading' && <Loading title='профиль' />}
			{status === 'success' && (
				<div
					style={{
						display: 'flex',
						flexDirection: 'column',
						alignItems: 'start',
						maxWidth: '800px',
						margin: '0 auto',
					}}
				>
					<Detail title='email' value={userData.email} />
					<Detail
						title='время'
						value={new Date(serverTime).toLocaleTimeString()}
					/>
					<Detail title='кол-во посещений' value={userData.loginCount} />
					<Detail title='роль' value={userData.role} />
					<div style={{ width: '200px', height: '200px' }}>
						<img
							style={{ width: '100%', height: '100%', objectFit: 'cover' }}
							src={`http://localhost:8080/images${userData.avatarUrl}`} // КОСТЯЛЕКККК
							alt='аватар'
						/>
					</div>

					<button
						style={{ padding: '10px', fontSize: '20px' }}
						onClick={handleClick}
					>
						выйти из аккаунта
					</button>
				</div>
			)}
			{status === 'error' && <Errors error={error} />}
		</Section>
	);
};

export default ProfilePage;
