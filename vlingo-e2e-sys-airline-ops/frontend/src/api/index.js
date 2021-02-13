export async function get(path) {
	const res = await fetch(path);
	return await res.json();
}

export async function post(path, body) {
	const res = await fetch(path, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json'},
		body: JSON.stringify(body)
	});
	return await res.json();
}

export async function put(path, body) {
	const res = await fetch(path, {
		method: 'PUT',
		headers: { 'Content-Type': 'application/json'},
		body: JSON.stringify(body)
	});
	return await res.json();
}

export async function patch(path, body) {
	const res = await fetch(path, {
		method: 'PATCH',
		headers: { 'Content-Type': 'application/json'},
		body: body,
	});
	return await res.json();
}

export const Api = { get, post, put, patch };