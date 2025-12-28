import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { getCategoriesThunk } from '../app/features/categories/categoriesThunk';
import List from '../components/List/List';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import Card from '../components/Card/Card';
import Header from '../components/Header/Header';

const CategoriesPage = () => {
  const { status, categoriesList, error } = useSelector(state => state.categories);

  // ✅ ИСПРАВЛЕНО - безопасная проверка
  const token = useSelector(state => state.users?.userData?.token || null);

  const dispatch = useDispatch();

  useEffect(() => {
    if (status === 'inactive') {
      dispatch(getCategoriesThunk());
    }
    if (status === 'error' && token) {
      dispatch(getCategoriesThunk());
    }
  }, [status, dispatch, token]);

  const navigate = useNavigate();

  const handleClick = (title) => {
    navigate(`/categories/${title}`);
  };

  return (
    <>
      {status === 'loading' && <Loading title="категории" />}
      {status === 'error' && <Errors error={error} />}

      {status === 'success' && (
        <List
          dataList={categoriesList}
          renderItem={(data) => (
            <Card
              key={data.id}
              data={data}
              type="catalog"
              onClick={() => handleClick(data.title)}
            />
          )}
        />
      )}
    </>
  );
};

export default CategoriesPage;
