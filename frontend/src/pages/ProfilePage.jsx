// import { useDispatch, useSelector } from 'react-redux';

import Header from '../components/Header/Header';
import Section from '../components/Section/Section';
// import Detail from '../components/Detail/Detail';
// import { useEffect } from 'react';
// import { getUser } from '../app/features/users/usersThunk';
// import Loading from '../components/Loading/Loading';
// import Errors from '../components/Errors/Error';

const ProfilePage = () => {
	// const { items, status, error } = useSelector(state => state.users);
	// const dispatch = useDispatch();
	// useEffect(() => {
	// 	if (status === 'inactive') dispatch(getUser('/auth/getInfo'));
	// }, [status, dispatch]);

	return (
		<Section>
			<Header title='Профиль' />

			{/*{status === 'loading' && <Loading title='профиль' />}*/}
			{/*{status === 'success' && <Detail title='email' value={items.email} />}*/}
			{/*{status === 'error' && <Errors error={error} />}*/}
		</Section>
	);
};

export default ProfilePage;
