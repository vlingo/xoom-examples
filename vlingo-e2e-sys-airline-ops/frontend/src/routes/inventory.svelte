<svelte:head>
	<title>Inventory</title>
</svelte:head>

<script>
	import { TextField, Select, Button, Dialog, Row } from 'svelte-materialify/src';
	import CardForm from '../components/CardForm.svelte';
	import { Api } from "../api";
	import { inventories } from "../stores/inventory.js";
	import { required } from "../util/validators.js";

	let isDialogActive = false;
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

	const submit= async () => {
		const res = await Api.post("/aircrafts/", formData);
		if (res) {
			$inventories = [...$inventories, res];
			toggleDialog();
		}
	}

	const toggleDialog = () => {
		isDialogActive = !isDialogActive;
	}

	$: valid = !!manufacturer && !!model && !!serialNumber && !!carrierName && !!carrierType && !!tailNumber;
</script>

<CardForm title="Inventories" nextLink="flight-planning" isNextDisabled={$inventories.length < 1}>
	<table class="mb-6">
		<thead>
			<tr>
				<th>#</th>
				<th>Manufacturer</th>
				<th>Model</th>
				<th>Serial Number</th>
				<th>Career Name</th>
				<th>Career Type</th>
				<th>Tail Number</th>
				<th>#</th>
			</tr>
		</thead>
		<tbody>
			{#each $inventories as inventory, ind (inventory.id)}
				<tr>
					<td>{ind + 1}</td>
					<td>{inventory.manufacturerSpecification.manufacturer}</td>
					<td> {inventory.manufacturerSpecification.model}</td>
					<td>{inventory.manufacturerSpecification.serialNumber}</td>
					<td>{inventory.carrier.name}</td>
					<td>{inventory.carrier.type}</td>
					<td>{inventory.registration.tailNumber}</td>
					<td></td>
				</tr>
			{/each}
		</tbody>
	</table>
	<Button on:click={toggleDialog}>New Inventory</Button>
	<Dialog persistent class="pa-8" bind:active={isDialogActive}>
		<form on:submit|preventDefault={submit}>
			<TextField outlined rules={[required]} bind:value={manufacturer}>Manufacturer</TextField>
			<TextField outlined rules={[required]} bind:value={model}>Model</TextField>
			<TextField outlined rules={[required]} bind:value={serialNumber}>Serial Number</TextField>
			<TextField outlined rules={[required]} bind:value={tailNumber}>Tail Number</TextField>
			<TextField outlined rules={[required]} bind:value={carrierName}>Carrier Name</TextField>
			<Select outlined rules={[required]} items={carrierTypes} bind:value={carrierType}>Carrier Type</Select>
			<Row class="ml-0 mr-0">
				<div style="flex:1; text-align: left;">
					<Button class="{valid ? 'success-color' : ''}" type="submit" disabled={!valid}>Create</Button>
				</div>
				<Button class="ml-3" type="reset">Reset</Button>
				<Button class="error-color  ml-3" on:click={toggleDialog}>Cancel</Button>
			</Row>
		</form>
	</Dialog>
</CardForm>