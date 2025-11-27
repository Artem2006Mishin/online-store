import styles from './List.module.css';

const List = ({ dataList, renderItem }) => {
	return (
		<div className={styles.list}>
			{dataList.map((data) => renderItem(data))}
		</div>
	);
};

export default List;
