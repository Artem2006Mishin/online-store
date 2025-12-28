import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import {
  getProductsThunk,
  getAllProductsThunk,
  createProductThunk,
  updateProductThunk,
  deleteProductThunk
} from '../app/features/products/productsThunk';
import Loading from '../components/Loading/Loading';
import Errors from '../components/Errors/Error';
import List from '../components/List/List';
import Product from '../components/Product/Product';
import { addToCart } from "../app/features/cart/cartSlice.js";
import Button from '../components/Button/Button';
import Input from '../components/Input/Input';
import Form from '../components/Form/Form';

const ProductsPage = () => {
  const { name } = useParams();
  const { status, productsList, error, allProductsList } = useSelector((state) => state.products);
  const dispatch = useDispatch();

  // Панель управления (для всех пользователей пока)
  const [showAdminPanel, setShowAdminPanel] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);

  useEffect(() => {
    dispatch(getProductsThunk(name));
  }, [name, dispatch]);

  useEffect(() => {
    if (showAdminPanel && allProductsList.length === 0) {
      dispatch(getAllProductsThunk());
    }
  }, [showAdminPanel, allProductsList.length, dispatch]);

  const cartItems = useSelector(state => state.cart.items);
  const cartIds = new Set(cartItems.map(item => item.productId));

  const handleClick = (productData) => {
    if (!cartIds.has(productData.id)) {
      dispatch(addToCart(productData));
    }
  };

  const handleCreateProduct = async (data) => {
    await dispatch(createProductThunk(data));
    dispatch(getAllProductsThunk());
  };

  const handleUpdateProduct = async (data) => {
    await dispatch(updateProductThunk({ id: editingProduct.id, productData: data }));
    dispatch(getAllProductsThunk());
    setEditingProduct(null);
  };

  const handleDeleteProduct = (id) => {
    if (confirm('Удалить товар?')) {
      dispatch(deleteProductThunk(id));
    }
  };

  const defaultValues = editingProduct ? {
    name: editingProduct.name,
    price: editingProduct.price,
    image: null,
    categoryId: editingProduct.categoryId
  } : {
    name: '',
    price: '',
    image: null,
    categoryId: ''
  };

  return (
    <>
      {status === 'loading' && <Loading title="товары" />}
      {status === 'error' && <Errors error={error} />}

      {status === 'success' && (
        <>
          {/* Кнопка панели управления - для всех */}
          <div style={{ textAlign: 'center', marginBottom: '30px' }}>
            <Button
              type="button"
              label={showAdminPanel ? 'Скрыть управление' : 'Управление товарами'}
              onClick={() => setShowAdminPanel(!showAdminPanel)}
            />
          </div>

          {/* Список товаров для пользователей */}
          <List
            dataList={productsList}
            renderItem={(data) => (
              <Product
                key={data.id}
                data={data}
                onClick={() => handleClick(data)}
              />
            )}
          />
        </>
      )}

      {/* ПАНЕЛЬ УПРАВЛЕНИЯ - для всех пользователей пока */}
      {showAdminPanel && (
        <div style={{ marginTop: '40px' }}>
          {/* Форма создания/редактирования */}
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
              color: '#1f2937'
            }}>
              {editingProduct ? 'Редактировать товар' : 'Добавить товар'}
            </h2>
            <Form
              onSubmit={editingProduct ? handleUpdateProduct : handleCreateProduct}
              defaultValues={defaultValues}
            >
              <Input label="Название" name="name" type="text" required />
              <Input label="Цена" name="price" type="number" step="0.01" required />
              <Input label="Фото" name="image" type="file" accept="image/*" />
              <Input label="ID категории" name="categoryId" type="number" />
              <div style={{ display: 'flex', gap: '10px', justifyContent: 'flex-end' }}>
                {editingProduct && (
                  <Button
                    type="button"
                    label="Отмена"
                    onClick={() => setEditingProduct(null)}
                    style={{ backgroundColor: '#6b7280', color: 'white' }}
                  />
                )}
                <Button type="submit" label="Сохранить" />
              </div>
            </Form>
          </div>

          {/* Таблица всех товаров */}
          {allProductsList.length > 0 && (
            <div style={{
              backgroundColor: '#ffffff',
              borderRadius: '20px',
              padding: '30px',
              boxShadow: '0 4px 20px rgba(0,0,0,0.08)'
            }}>
              <h2 style={{
                fontSize: '28px',
                fontWeight: '700',
                marginBottom: '25px',
                color: '#1f2937'
              }}>
                Все товары ({allProductsList.length})
              </h2>
              <div style={{ overflowX: 'auto' }}>
                <table style={{
                  width: '100%',
                  borderCollapse: 'collapse',
                  fontSize: '14px'
                }}>
                  <thead>
                    <tr style={{ backgroundColor: '#f8fafc' }}>
                      <th style={{ padding: '15px 10px', textAlign: 'left' }}>ID</th>
                      <th style={{ padding: '15px 10px', textAlign: 'left' }}>Название</th>
                      <th style={{ padding: '15px 10px', textAlign: 'right' }}>Цена</th>
                      <th style={{ padding: '15px 10px', textAlign: 'center' }}>Действия</th>
                    </tr>
                  </thead>
                  <tbody>
                    {allProductsList.map((product) => (
                      <tr key={product.id} style={{ borderBottom: '1px solid #e5e7eb' }}>
                        <td style={{ padding: '15px 10px' }}>{product.id}</td>
                        <td style={{ padding: '15px 10px' }}>{product.name}</td>
                        <td style={{ padding: '15px 10px', textAlign: 'right' }}>
                          {product.price} ₽
                        </td>
                        <td style={{ padding: '15px 10px', textAlign: 'center' }}>
                          <div style={{ display: 'flex', gap: '8px', justifyContent: 'center' }}>
                            <Button
                              type="button"
                              label="✏️ Изменить"
                              onClick={() => setEditingProduct(product)}
                              style={{
                                padding: '8px 12px',
                                fontSize: '12px',
                                backgroundColor: '#3b82f6',
                                minWidth: 'auto'
                              }}
                            />
                            <Button
                              type="button"
                              label="🗑️ Удалить"
                              onClick={() => handleDeleteProduct(product.id)}
                              style={{
                                padding: '8px 12px',
                                fontSize: '12px',
                                backgroundColor: '#ef4444',
                                minWidth: 'auto'
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
        </div>
      )}
    </>
  );
};

export default ProductsPage;
