import styles from './List.module.css';
import Card from '../Card/Card';

const List = ({ dataList, cardType }) => {
	return (
		<div className={styles.list}>
			{dataList.map((data) => (
				<Card key={data.id} data={data} type={cardType} />
			))}
		</div>
	);
};

export default List;
