<svelte:head>
	<title>Inventory</title>
</svelte:head>

<script>
	import CardForm from '../components/CardForm.svelte';
	import { TextField } from 'svelte-materialify/src';
	import VlSelect from '../components/VlSelect.svelte';
	import { Api } from "../api";
	import { inventories } from "../stores";
	import { required } from "../util/validators.js";

	const carrierTypes = [
    { name: 'Airline', value: 'AIRLINE' },
    { name: 'Shipping', value: 'SHIPPING' },
  ];

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

	let valid = false;
	let cardForm;
	const submit= async () => {
		const res = await Api.post("/aircrafts/", formData);
		if (res) {
			$inventories = [...$inventories, res];
			cardForm.toggleDialog();
		}
	}

	$: valid = !!manufacturer && !!model && !!serialNumber && !!carrierName && !!carrierType && !!tailNumber;
</script>

<CardForm
	title="Inventories"
	buttonText="New Inventory"
	nextLink="flight-planning"
	isNextDisabled={$inventories.length < 1}
	showNoContent={$inventories.length < 1}
	{valid}
	on:submit={submit}
	bind:this={cardForm}
>
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
	<div slot="dialog-form">
		<TextField outlined rules={[required]} bind:value={manufacturer}>Manufacturer</TextField>
		<TextField outlined rules={[required]} bind:value={model}>Model</TextField>
		<TextField outlined rules={[required]} bind:value={serialNumber}>Serial Number</TextField>
		<TextField outlined rules={[required]} bind:value={tailNumber}>Tail Number</TextField>
		<TextField outlined rules={[required]} bind:value={carrierName}>Carrier Name</TextField>
		<VlSelect outlined rules={[required]} items={carrierTypes} bind:value={carrierType}>Carrier Type</VlSelect>
	</div>
</CardForm>