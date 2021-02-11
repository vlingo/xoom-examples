<svelte:head>
	<title>Air Traffic Control</title>
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

	const statuses = [
    { name: 'Departed Gate', value: 'DepartedGate' },
    { name: 'Outbouind Taxiing', value: 'OutbouindTaxiing' },
		{ name: 'In Flight Line', value: 'InFlightLine' },
		{ name: 'Cleared For Take Off', value: 'ClearedForTakeOff' },
		{ name: 'In Flight', value: 'InFlight' },
		{ name: 'Landing Pattern', value: 'LandingPattern' },
		{ name: 'Cleared For Landing', value: 'ClearedForLanding' },
		{ name: 'Landed', value: 'Landed' },
		{ name: 'Arrived At Gate', value: 'ArrivedAtGate' },
	]
	let valid = false;
	let formData = {
		aircraftId: "05e5b41c-1fc7-4946-b04a-fb7a43d9d119",
		number: 1983,
		tailNumber: 2011,
		equipment: "RPC AC INTERNATIONAL"
	}

	let statusChangeFormData = {
		number: 1983,
		status: "LANDED"
	}


</script>

<CardForm title="Air Traffic Control" prevLink="airport-terminal" nextLink="aircraft-monitoring" isNextDisabled={false}>
	<TextField outlined>Gate Agent Name</TextField>
	<Select outlined items={aircrafts}>Flight</Select>

	<Select outlined items={statuses}>Equipment - Terminal</Select>
</CardForm>


