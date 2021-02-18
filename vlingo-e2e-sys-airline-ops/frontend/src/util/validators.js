const errors = Object.freeze({
	EMPTY: "May not be empty",
});

export const required = (v) => {
  return !!v || errors.EMPTY;
};
