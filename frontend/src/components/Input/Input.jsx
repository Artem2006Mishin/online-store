import {useFormContext} from 'react-hook-form';
import styles from './Input.module.css';

const Input = ({label, name, type = 'text' , accept}) => {
  const {
    register,
    formState: {errors},
  } = useFormContext();

  const isFile = type === 'file';

  return (
    <div className={styles.filed}>
      <div>
        <label className={styles.filed__title}>{label}</label>
      </div>
      <input
        className={styles.filed__input}
        type={type}
        accept={isFile ? accept : undefined}
        {...register(name)}
      />
      {errors[name] && <p style={{color: 'red'}}>{errors[name].message}</p>}
    </div>
  );
};

export default Input;
