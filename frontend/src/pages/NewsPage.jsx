import { useDispatch, useSelector } from 'react-redux';
import { useEffect, useState } from 'react';

import { getNewsThunk } from '../app/features/news/newsThunk';

import Header from '../components/Header/Header';
import List from '../components/List/List';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import Section from '../components/Section/Section';
import Card from '../components/Card/Card';
import Button from '../components/Button/Button';
import NewsForm from '../components/NewsForm/NewsForm';

const NewsPage = () => {
	const { status, newsList, error } = useSelector((state) => state.news);
	const { userData } = useSelector((state) => state.users);
	const dispatch = useDispatch();
	const [showForm, setShowForm] = useState(false);
	const [editingNews, setEditingNews] = useState(null);

	const isModerator = userData?.role === 'MODERATOR';

	useEffect(() => {
		if (status === 'inactive') dispatch(getNewsThunk());
	}, [status, dispatch]);

	const handleEdit = (news) => {
		setEditingNews(news);
		setShowForm(true);
	};

	const handleCancel = () => {
		setShowForm(false);
		setEditingNews(null);
	};

	return (
		<Section>
			<Header title='Новости' />

			{status === 'loading' && <Loading title={'новости'} />}
			{status === 'error' && <Errors error={error} />}

			{status === 'success' && (
				<List
					dataList={newsList}
					renderItem={(data) => (
						<Card
							key={data.id}
							data={data}
							type='news'
							onEdit={isModerator ? handleEdit : null}
							showButtons={isModerator}
						/>
					)}
				/>
			)}

			{isModerator && !showForm && (
				<Button
					type='button'
					label='Добавить новость'
					onClick={() => setShowForm(true)}
				/>
			)}

			{showForm && (
				<NewsForm
					onCancel={handleCancel}
					initialValues={
						editingNews
							? {
									title: editingNews.title,
									text: editingNews.text,
									image: null,
							  }
							: null
					}
					isEditing={!!editingNews}
					newsId={editingNews?.id}
				/>
			)}
		</Section>
	);
};

export default NewsPage;
