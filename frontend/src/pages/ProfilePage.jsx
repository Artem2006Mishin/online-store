import { useSelector } from 'react-redux';

import Header from '../components/Header/Header';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import Section from '../components/Section/Section';
import Detail from '../components/Detail/Detail';

const ProfilePage = () => {
	const items = useSelector((state) => state.users.items);
	const status = useSelector((state) => state.users.status);
	const error = useSelector((state) => state.users.error);

	return (
		<Section>
			<Header title='Профиль' />

			{status === 'loading' && <Loading title={'новости'} />}
			{status === 'success' && <Detail title='email' value={items.email} />}
			{status === 'error' && <Errors error={error} />}
		</Section>
	);
};

export default ProfilePage;
