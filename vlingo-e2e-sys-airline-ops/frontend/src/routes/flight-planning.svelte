<svelte:head>
	<title>Flight Planning</title>
</svelte:head>

<script>
	import { TextField, Select } from 'svelte-materialify/src';
	import CardForm from '../components/CardForm.svelte';

	const aircrafts = [
    { name: 'AirCraft1', value: 'AirCraft1' },
    { name: 'AirCraft2', value: 'AirCraft2' },
  ];
	let valid = false;
</script>

<CardForm title="Flight Planning" prevLink="inventory" nextLink="fleet-crew" isNextDisabled={false}>
	<Select outlined items={aircrafts}>Aircraft</Select>
	<TextField outlined>Departure Date</TextField>
	<TextField outlined>Departure Airport</TextField>
	<TextField outlined>Arrival Date</TextField>
	<TextField outlined>Arrival Airport</TextField>
</CardForm>