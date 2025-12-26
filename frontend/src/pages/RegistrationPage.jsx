import {useDispatch, useSelector} from 'react-redux';
import {useNavigate} from 'react-router-dom';
import {useEffect} from 'react';
import {registerSchema} from '../components/schema';
import {authUserThunk} from '../app/features/users/usersThunk';
import Input from '../components/Input/Input';
import Form from '../components/Form/Form';
import Button from '../components/Button/Button';
import Loading from "../components/Loading/Loading.jsx";

const RegistrationPage = () => {
  const defaultValues = {
    email: '',
    password: '',
    confirmPassword: '',
    avatar: null,
  };

  const dispatch = useDispatch();
  const onSubmit = (data) => {
    const {confirmPassword: _, avatar, ...rest} = data;
    const formData = new FormData();

    Object.entries(rest).forEach(([key, value]) => {
      formData.append(key, value);
    });

    if (avatar && avatar.length > 0) {
      formData.append('avatar', avatar[0]);
    }

    dispatch(
      authUserThunk({
        url: 'register',
        userData: formData,
        isMultipart: true,
      })
    );
  };


  const navigate = useNavigate();
  const status = useSelector(state => state.users.status);
  useEffect(() => {
    if (status === 'success') navigate('/profile', {replace: true});
  }, [status, navigate]);

  return (
    <>
      {status === 'loading' && <Loading title='профиль'/>}
      {status !== 'loading' && (
        <Form
          onSubmit={onSubmit}
          defaultValues={defaultValues}
          schema={registerSchema}
        >
          <Input label='Электронная почта' name='email' type='email'/>
          <Input label='Аватар' name='avatar' type='file' accept="image/*"/>

          <Input label='Пароль' name='password' type='password'/>
          <Input
            label='Подтверждение пароля'
            name='confirmPassword'
            type='password'
          />

          <Button type='submit' label='Зарегистрироваться'/>
          <Button type='button' label='Вернуться обратно' to='/auth'/>
        </Form>
      )}
    </>
  );
};

export default RegistrationPage;
