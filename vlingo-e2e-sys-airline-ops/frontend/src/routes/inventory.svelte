<svelte:head>
	<title>Inventory</title>
</svelte:head>

<script>
	import { TextField, Select } from 'svelte-materialify/src';
	import CardForm from '../components/CardForm.svelte';

	const carrierType = [
    { name: 'Airline', value: 'AIRLINE' },
    { name: 'Shipping', value: 'SHIPPING' },
  ];
	let valid = false;
</script>

<CardForm title="Inventory" nextLink="flight-planning" isNextDisabled={false}>
	<TextField outlined>Manufacturer</TextField>
	<TextField outlined>Model</TextField>
	<TextField outlined>Serial Number</TextField>
	<TextField outlined>Carrier</TextField>
	<Select multiple outlined items={carrierType}>Carrier Type</Select>
	<TextField outlined>Tail Number</TextField>
</CardForm>