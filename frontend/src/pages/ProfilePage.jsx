import Header from '../components/Header/Header';
import Section from '../components/Section/Section';
import Detail from '../components/Detail/Detail';
import { useEffect, useState } from 'react';
import {
	getUserThunk,
	updateProfileThunk,
	deleteUserThunk,
	changeUserPasswordThunk
} from '../app/features/users/usersThunk.js';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import { useDispatch, useSelector } from 'react-redux';
import { getTimeThunk } from '../app/features/time/timeThunk.js';
import { tick } from '../app/features/time/timeSlice.js';
import { resetCart } from '../app/features/users/usersSlice.js';
import { useNavigate } from 'react-router-dom';
import { getOrdersThunk } from '../app/features/order/orderThunk.js';
import { updateProfileSchema } from '../components/schema';
import Input from '../components/Input/Input';
import Form from '../components/Form/Form';
import Button from '../components/Button/Button';


const ProfilePage = () => {
	const {
		userData,
		status,
		error,
		allUsersList  // ← Только это!
	} = useSelector((state) => state.users);

	console.log('🔍 ProfilePage DEBUG:', {
		status,
		userData,
		allUsersList,
		allUsersListLength: allUsersList?.length,
		hasUsers: allUsersList?.length > 0,
		userDataId: userData?.id
	});

	const { serverTime } = useSelector((state) => state.time);
	const dispatch = useDispatch();
	const [isEditing, setIsEditing] = useState(false);

	const { getOrdersStatus, ordersList, getOrdersError } = useSelector(
		(state) => state.orders
	);

	useEffect(() => {
		if (status === 'inactive') dispatch(getUserThunk());
	}, [status, dispatch]);

	useEffect(() => {
		if (getOrdersStatus === 'inactive') dispatch(getOrdersThunk());
	}, [getOrdersStatus, dispatch]);

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

	const defaultValues = {
		email: userData?.email || '',
		password: '',
		confirmPassword: '',
		avatar: null,
	};

	const onSubmit = (data) => {
		const { confirmPassword: _, avatar, ...rest } = data;
		const formData = new FormData();

		Object.entries(rest).forEach(([key, value]) => {
			if (value) formData.append(key, value);
		});

		if (avatar && avatar.length > 0) {
			formData.append('avatar', avatar[0]);
		}

		dispatch(
			updateProfileThunk({
				userData: formData,
				isMultipart: true,
			})
		).then((result) => {
			if (result.meta.requestStatus === 'fulfilled') {
				setIsEditing(false);
				// dispatch(getUserThunk()); // Убираем, данные уже обновлены в fulfilled
			}
		});
	};

	return (
		<Section>
			<Header title='Профиль' />

			{status === 'loading' && <Loading title='профиль' />}
			{(status === 'success' ||
				(status === 'error' && error?.status === 'EMAIL_BUSY')) && (
					<div
						style={{
							maxWidth: '1200px',
							margin: '0 auto',
							padding: '20px',
						}}
					>
						{/* Карточка профиля */}
						<div
							style={{
								background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
								borderRadius: '20px',
								padding: '40px',
								marginBottom: '30px',
								boxShadow: '0 10px 40px rgba(0, 0, 0, 0.1)',
								color: 'white',
							}}
						>
							<div
								style={{
									display: 'flex',
									alignItems: 'center',
									gap: '30px',
									flexWrap: 'wrap',
								}}
							>
								{/* Аватар */}
								{userData.avatarUrl && (
									<div
										style={{
											width: '150px',
											height: '150px',
											borderRadius: '50%',
											overflow: 'hidden',
											border: '5px solid rgba(255, 255, 255, 0.3)',
											boxShadow: '0 5px 20px rgba(0, 0, 0, 0.2)',
											flexShrink: 0,
										}}
									>
										<img
											style={{
												width: '100%',
												height: '100%',
												objectFit: 'cover',
											}}
											src={`http://localhost:8080${userData.avatarUrl}`}
											alt='аватар'
											onError={(e) => {
												console.error(
													'Ошибка загрузки аватара. URL:',
													`http://localhost:8080${userData.avatarUrl}`
												);
												console.error('Данные пользователя:', userData);
												e.target.style.display = 'none';
											}}
											onLoad={() => {
												console.log(
													'Аватар успешно загружен:',
													`http://localhost:8080${userData.avatarUrl}`
												);
											}}
										/>
									</div>
								)}

								{/* Информация о пользователе */}
								<div style={{ flex: 1, minWidth: '250px' }}>
									<h1
										style={{
											fontSize: '32px',
											fontWeight: '700',
											marginBottom: '10px',
											textShadow: '0 2px 10px rgba(0, 0, 0, 0.2)',
										}}
									>
										{userData.email}
									</h1>
									<div
										style={{
											display: 'flex',
											flexWrap: 'wrap',
											gap: '20px',
											marginTop: '20px',
										}}
									>
										<div
											style={{
												backgroundColor: 'rgba(255, 255, 255, 0.2)',
												padding: '10px 20px',
												borderRadius: '10px',
												backdropFilter: 'blur(10px)',
											}}
										>
											<div
												style={{
													fontSize: '12px',
													opacity: 0.9,
													marginBottom: '5px',
												}}
											>
												Роль
											</div>
											<div style={{ fontSize: '16px', fontWeight: '600' }}>
												{userData.role}
											</div>
										</div>
										<div
											style={{
												backgroundColor: 'rgba(255, 255, 255, 0.2)',
												padding: '10px 20px',
												borderRadius: '10px',
												backdropFilter: 'blur(10px)',
											}}
										>
											<div
												style={{
													fontSize: '12px',
													opacity: 0.9,
													marginBottom: '5px',
												}}
											>
												Посещений
											</div>
											<div style={{ fontSize: '16px', fontWeight: '600' }}>
												{userData.loginCount}
											</div>
										</div>
										<div
											style={{
												backgroundColor: 'rgba(255, 255, 255, 0.2)',
												padding: '10px 20px',
												borderRadius: '10px',
												backdropFilter: 'blur(10px)',
											}}
										>
											<div
												style={{
													fontSize: '12px',
													opacity: 0.9,
													marginBottom: '5px',
												}}
											>
												Время
											</div>
											<div style={{ fontSize: '16px', fontWeight: '600' }}>
												{new Date(serverTime).toLocaleTimeString()}
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						{/* Кнопка редактирования профиля */}
						<div style={{ marginBottom: '30px', textAlign: 'center' }}>
							<button
								onClick={() => setIsEditing(!isEditing)}
								style={{
									padding: '12px 30px',
									fontSize: '16px',
									fontWeight: '600',
									color: '#fff',
									backgroundColor: '#3b82f6',
									border: 'none',
									borderRadius: '10px',
									cursor: 'pointer',
									transition: 'all 0.3s ease',
									boxShadow: '0 4px 15px rgba(59, 130, 246, 0.3)',
								}}
								onMouseEnter={(e) => {
									e.target.style.backgroundColor = '#2563eb';
									e.target.style.transform = 'translateY(-2px)';
									e.target.style.boxShadow = '0 6px 20px rgba(59, 130, 246, 0.4)';
								}}
								onMouseLeave={(e) => {
									e.target.style.backgroundColor = '#3b82f6';
									e.target.style.transform = 'translateY(0)';
									e.target.style.boxShadow = '0 4px 15px rgba(59, 130, 246, 0.3)';
								}}
							>
								{isEditing ? 'Отменить редактирование' : 'Редактировать профиль'}
							</button>
						</div>

						{/* Форма редактирования профиля */}
						{isEditing && (
							<div
								style={{
									backgroundColor: '#ffffff',
									borderRadius: '20px',
									padding: '30px',
									marginBottom: '30px',
									boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
								}}
							>
								<h2
									style={{
										fontSize: '28px',
										fontWeight: '700',
										marginBottom: '25px',
										color: '#1f2937',
										display: 'flex',
										alignItems: 'center',
										gap: '10px',
									}}
								>
									<span
										style={{
											width: '4px',
											height: '28px',
											backgroundColor: '#667eea',
											borderRadius: '2px',
										}}
									/>
									Редактировать профиль
								</h2>

								<Form
									onSubmit={onSubmit}
									defaultValues={defaultValues}
									schema={updateProfileSchema}
								>
									<Input label='Электронная почта' name='email' type='email' />
									<Input
										label='Новый пароль'
										name='password'
										type='password'
										placeholder='Оставьте пустым, если не хотите менять'
									/>
									<Input
										label='Подтверждение нового пароля'
										name='confirmPassword'
										type='password'
										placeholder='Оставьте пустым, если не хотите менять'
									/>
									<Input
										label='Новый аватар'
										name='avatar'
										type='file'
										accept='image/*'
									/>

									<Button type='submit' label='Сохранить изменения' />
								</Form>
							</div>
						)}

						{/* Кнопка выхода */}
						<div style={{ marginBottom: '30px', textAlign: 'center' }}>
							<button
								onClick={handleClick}
								style={{
									padding: '12px 30px',
									fontSize: '16px',
									fontWeight: '600',
									color: '#fff',
									backgroundColor: '#ef4444',
									border: 'none',
									borderRadius: '10px',
									cursor: 'pointer',
									transition: 'all 0.3s ease',
									boxShadow: '0 4px 15px rgba(239, 68, 68, 0.3)',
								}}
								onMouseEnter={(e) => {
									e.target.style.backgroundColor = '#dc2626';
									e.target.style.transform = 'translateY(-2px)';
									e.target.style.boxShadow = '0 6px 20px rgba(239, 68, 68, 0.4)';
								}}
								onMouseLeave={(e) => {
									e.target.style.backgroundColor = '#ef4444';
									e.target.style.transform = 'translateY(0)';
									e.target.style.boxShadow = '0 4px 15px rgba(239, 68, 68, 0.3)';
								}}
							>
								Выйти из аккаунта
							</button>
						</div>

						{/* Секция заказов */}
						<div
							style={{
								backgroundColor: '#ffffff',
								borderRadius: '20px',
								padding: '30px',
								boxShadow: '0 4px 20px rgba(0, 0, 0, 0.08)',
							}}
						>
							<h2
								style={{
									fontSize: '28px',
									fontWeight: '700',
									marginBottom: '25px',
									color: '#1f2937',
									display: 'flex',
									alignItems: 'center',
									gap: '10px',
								}}
							>
								<span
									style={{
										width: '4px',
										height: '28px',
										backgroundColor: '#667eea',
										borderRadius: '2px',
									}}
								/>
								Мои заказы
							</h2>

							{getOrdersStatus === 'loading' && <Loading title='заказы' />}
							{getOrdersStatus === 'succeeded' && (
								<div>
									{ordersList && ordersList.length > 0 ? (
										<div
											style={{
												display: 'grid',
												gridTemplateColumns:
													'repeat(auto-fill, minmax(300px, 1fr))',
												gap: '20px',
											}}
										>
											{ordersList.map((order) => (
												<div
													key={order.id}
													style={{
														backgroundColor: '#f8fafc',
														padding: '20px',
														borderRadius: '15px',
														border: '1px solid #e5e7eb',
														transition: 'all 0.3s ease',
														cursor: 'pointer',
													}}
													onMouseEnter={(e) => {
														e.currentTarget.style.transform = 'translateY(-5px)';
														e.currentTarget.style.boxShadow =
															'0 10px 25px rgba(0, 0, 0, 0.1)';
														e.currentTarget.style.borderColor = '#667eea';
													}}
													onMouseLeave={(e) => {
														e.currentTarget.style.transform = 'translateY(0)';
														e.currentTarget.style.boxShadow = 'none';
														e.currentTarget.style.borderColor = '#e5e7eb';
													}}
												>
													<div
														style={{
															display: 'flex',
															justifyContent: 'space-between',
															alignItems: 'center',
															marginBottom: '15px',
														}}
													>
														<span
															style={{
																fontSize: '12px',
																color: '#6b7280',
																fontWeight: '500',
															}}
														>
															Заказ #{order.id}
														</span>
														<span
															style={{
																fontSize: '20px',
																fontWeight: '700',
																color: '#667eea',
															}}
														>
															{order.totalPrice.toFixed(2)} ₽
														</span>
													</div>
													<div
														style={{
															paddingTop: '15px',
															borderTop: '1px solid #e5e7eb',
														}}
													>
														<div
															style={{
																fontSize: '14px',
																color: '#6b7280',
																display: 'flex',
																alignItems: 'center',
																gap: '8px',
															}}
														>
															<span>📅</span>
															<span>
																{order.createdAt
																	? new Date(order.createdAt).toLocaleString(
																		'ru-RU',
																		{
																			day: '2-digit',
																			month: '2-digit',
																			year: 'numeric',
																			hour: '2-digit',
																			minute: '2-digit',
																		}
																	)
																	: 'Не указана'}
															</span>
														</div>
													</div>
												</div>
											))}
										</div>
									) : (
										<div
											style={{
												textAlign: 'center',
												padding: '60px 20px',
												color: '#6b7280',
											}}
										>
											<div
												style={{
													fontSize: '64px',
													marginBottom: '20px',
												}}
											>
												📦
											</div>
											<p
												style={{
													fontSize: '18px',
													fontWeight: '500',
												}}
											>
												У вас пока нет заказов
											</p>
											<p
												style={{
													fontSize: '14px',
													marginTop: '10px',
													opacity: 0.7,
												}}
											>
												Сделайте первый заказ, чтобы он появился здесь
											</p>
										</div>
									)}
								</div>
							)}
							{getOrdersStatus === 'failed' && <Errors error={getOrdersError} />}
						</div>
					</div>
				)}
			{status === 'error' && error?.status !== 'EMAIL_BUSY' && (
				<Errors error={error} />
			)}
			{/* ТАБЛИЦА ПОСЛЕ ВСЕГО, ПЕРЕД </Section> */}
			{status === 'success' && (
				<div style={{
					backgroundColor: '#ffffff',
					borderRadius: '20px',
					padding: '30px',
					boxShadow: '0 4px 20px rgba(0,0,0,0.08)',
					marginBottom: '30px'
				}}>
					<h2 style={{
						fontSize: '28px',
						fontWeight: '700',
						marginBottom: '25px',
						color: '#1f2937',
						display: 'flex',
						alignItems: 'center',
						gap: '10px'
					}}>
						<span style={{ width: '4px', height: '28px', backgroundColor: '#667eea', borderRadius: '2px' }} />
						Все пользователи ({allUsersList.length})
					</h2>
					<div style={{ overflowX: 'auto' }}>
						<table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '14px' }}>
							<thead>
								<tr style={{ backgroundColor: '#f8fafc' }}>
									<th style={{ padding: '15px', textAlign: 'left', borderBottom: '2px solid #e5e7eb', fontWeight: '600' }}>
										Email
									</th>
								</tr>
							</thead>
							<tbody>
								{allUsersList
									.filter(user => user.id !== userData?.id)
									.map((user) => (
										<tr key={user.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
											<td style={{ padding: '15px', color: '#374151' }}>
												{user.email}
											</td>
											<td style={{ padding: '15px', textAlign: 'center' }}>
												<div style={{ display: 'flex', gap: '10px', justifyContent: 'center' }}>
													{/* Кнопка изменения пароля */}
													<Button
														type="button"
														label="Сменить пароль"
														onClick={() => {
															const newPassword = prompt('Новый пароль:');
															if (newPassword) {
																dispatch(changeUserPasswordThunk({ userId: user.id, password: newPassword }));
															}
														}}
														style={{ padding: '8px 16px', fontSize: '12px' }}
													/>
													{/* Кнопка удаления */}
													<Button
														type="button"
														label="Удалить"
														onClick={() => {
															if (confirm(`Удалить ${user.email}?`)) {
																dispatch(deleteUserThunk(user.id));
															}
														}}
														style={{
															padding: '8px 16px',
															fontSize: '12px',
															backgroundColor: '#ef4444',
															color: 'white'
														}}
													/>
												</div>
											</td>
										</tr>
									))}
							</tbody>

						</table>
					</div>
				</div>
			)}

			{status === 'error' && error?.status !== 'EMAIL_BUSY' && <Errors error={error} />}
		</Section>
	);
};

export default ProfilePage;
