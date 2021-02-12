<svelte:head>
	<title>Fleet Crew</title>
</svelte:head>

<script>
	import { TextField, Select } from 'svelte-materialify/src';
	import CardForm from '../components/CardForm.svelte';

	const aircrafts = [
    { name: 'AirCraft1', value: 'AirCraft1' },
    { name: 'AirCraft2', value: 'AirCraft2' },
  ];

	let valid = false;

	let formData = {
		aircraftId: "05e5b41c-1fc7-4946-b04a-fb7a43d9d119",
		carrier: "EMB",
		flightNumber: "1983",
		tailNumber: "2011"
	}

	const submit= async () => {
		const res = await Api.post("/fleetcrew/aircrafts/", formData);
		console.log(res);
	}
</script>

<CardForm title="Fleet Crew" prevLink="flight-planning" nextLink="airport-terminal" isNextDisabled={false}>
	<form on:submit|preventDefault={submit}>
		<Select outlined items={aircrafts} bind:value={formData.aircraftId}>Aircraft</Select>
		<TextField outlined bind:value={formData.flightNumber}>Flight Number</TextField>
		<TextField outlined bind:value={formData.tailNumber}>Tail Number</TextField>
		<TextField outlined bind:value={formData.carrier}>Carrier Name</TextField>
	</form>
</CardForm>
