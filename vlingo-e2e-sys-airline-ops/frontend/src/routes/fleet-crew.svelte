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

	const agents = [
    { name: 'AirCraft1', value: 'AirCraft1' },
    { name: 'AirCraft2', value: 'AirCraft2' },
  ];

	const fleetCrewStatus = [
    { name: 'AirCraft1', value: 'AirCraft1' },
    { name: 'AirCraft2', value: 'AirCraft2' },
	]
	let valid = false;
</script>

<CardForm title="Fleet Crew" prevLink="flight-planning" nextLink="airport-terminal" isNextDisabled={false}>
	<Select outlined items={aircrafts}>Aircraft</Select>
	<Select outlined items={agents}>Agent</Select>
	<Select outlined items={fleetCrewStatus}>Status</Select>
</CardForm>
