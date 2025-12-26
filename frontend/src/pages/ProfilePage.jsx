import Header from '../components/Header/Header';
import Section from '../components/Section/Section';
import Detail from '../components/Detail/Detail';
import { useEffect } from 'react';
import { getUserThunk } from '../app/features/users/usersThunk';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import {useDispatch, useSelector} from "react-redux";
import {getTimeThunk} from "../app/features/time/timeThunk.js";
import {tick} from "../app/features/time/timeSlice.js";

const ProfilePage = () => {
	const { userData, status, error } = useSelector(state => state.users);
  const {serverTime} = useSelector(state => state.time);
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

	return (
		<Section>
			<Header title='Профиль' />

			{status === 'loading' && <Loading title='профиль' />}
			{status === 'success' &&
        <div>
          <Detail title='email' value={userData.email} />
          <Detail title='время' value={new Date(serverTime).toLocaleTimeString()} />
        </div>
        }
			{status === 'error' && <Errors error={error} />}
		</Section>
	);
};

export default ProfilePage;
