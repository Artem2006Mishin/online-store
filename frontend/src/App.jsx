import { useState, useEffect } from 'react';
import api from './api/axios';

const App = () => {
	const [users, setUsers] = useState([]);

	useEffect(() => {
		const loadUsers = async () => {
			const response = await api.get('/users');
			setUsers(response.data);
		};

		loadUsers();
	}, []);

	const handleAddUser = async () => {
		const response = await api.post('/users', { name: 'artem' });
		setUsers([...users, response.data]);
	};

	return (
		<div>
			<ul>
				{users.map((u) => (
					<li key={u.id}>имя: {u.name}</li>
				))}
			</ul>

			<button onClick={handleAddUser}>добавить артема</button>
		</div>
	);
};

export default App;
