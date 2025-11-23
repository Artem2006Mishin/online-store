export const handleGettingElements = (builder, asyncThunk) => {
	builder
		.addCase(asyncThunk.pending, (state) => {
			state.status = 'loading';
		})
		.addCase(asyncThunk.fulfilled, (state, action) => {
			state.status = 'success';
			state.items = action.payload;
		})
		.addCase(asyncThunk.rejected, (state, action) => {
			state.status = 'error';
			state.error = action.payload;
		});
};
