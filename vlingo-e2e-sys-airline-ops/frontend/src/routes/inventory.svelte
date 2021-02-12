<svelte:head>
	<title>Inventory</title>
</svelte:head>

<script>
	import { TextField, Select, Button } from 'svelte-materialify/src';
	import CardForm from '../components/CardForm.svelte';
	import { Api } from "../api";
import { onMount } from 'svelte';
	const carrierTypes = [
    { name: 'Airline', value: 'AIRLINE' },
    { name: 'Shipping', value: 'SHIPPING' },
  ];
	let valid = false;
	let manufacturer = "EMB";
	let model = "E1-190";
	let serialNumber = "1515";
	let carrierName = "TAM";
	let carrierType = "AIRLINE";
	let tailNumber = "VGO1616";
	$: formData = {
		registration: {
			tailNumber
		},
		manufacturerSpecification : {
			manufacturer,
			model,
			serialNumber
		},
		carrier: {
			name: carrierName,
			type: carrierType
		}
	}

	onMount(async () => {
		const res = await Api.get("/aircrafts");
		console.log(res);
	})

	const submit= async () => {
		const res = await Api.post("/aircrafts", formData);
		console.log(res);
	}
</script>

<CardForm title="Inventory" nextLink="flight-planning" isNextDisabled={false}>
	<form on:submit|preventDefault={submit}>
		<TextField outlined bind:value={manufacturer}>Manufacturer</TextField>
		<TextField outlined bind:value={model}>Model</TextField>
		<TextField outlined bind:value={serialNumber}>Serial Number</TextField>
		<TextField outlined bind:value={tailNumber}>Tail Number</TextField>
		<TextField outlined bind:value={carrierName}>Carrier Name</TextField>
		<Select outlined items={carrierTypes} bind:value={carrierType}>Carrier Type</Select>
		<Button type="submit">Create</Button>
	</form>
</CardForm>