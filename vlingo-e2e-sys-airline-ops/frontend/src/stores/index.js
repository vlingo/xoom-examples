import { writable } from 'svelte/store';

function isMobileStore() {
	const { subscribe, set } = writable(false);
	return {
		subscribe,
		set,
		check: () => {
			import('svelte-materialify/src/utils/breakpoints').then(({ default: breakpoints }) => {
				set(window.matchMedia(breakpoints['md-and-down']).matches);
			});
		},
	};
};

export const isMobile = isMobileStore();

export function createLocalStore(key, initialValue) {
	const localValue = process.browser ? localStorage.getItem(key) : initialValue;
	const { subscribe, set } = writable(localValue);

	return {
		subscribe,
		set: (value) => {
			if (process.browser) {
				localStorage.setItem(key, value);
			}
			set(value)
		},
	};
}

export const theme = createLocalStore('theme', 'light');

export const controls = writable([]);
export const terminals = writable([]);
export const fleetcrews = writable([]);
export const flights = writable([]);
export const inventories = writable([]);
