export async function get(path) {
	const res = await fetch(path);
	return res;
}

export async function post(path, body) {
	const res = await fetch(path, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json'},
		body: JSON.stringify(body)
	});
	return res;
}

export async function put(path, body) {
	const res = await fetch(path, {
		method: 'PUT',
		headers: { 'Content-Type': 'application/json'},
		body: JSON.stringify(body)
	});
	return res;
}

export async function patch(path, body) {
	const res = await fetch(path, {
		method: 'PATCH',
		headers: { 'Content-Type': 'application/json'},
		body: body,
	});
	return res;
}

export const Api = { get, post, put, patch };