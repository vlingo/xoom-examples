<svelte:head>
	<title>Air Traffic Control</title>
</svelte:head>

<script>
import { onMount } from 'svelte';

	import { TextField, Select, Button, Dialog, Row } from 'svelte-materialify/src';
	import CardForm from '../components/CardForm.svelte';
	import VlSelect from '../components/VlSelect.svelte';
	import { Api } from "../api";
	import { inventories } from "../stores/inventory.js";
	import { controls } from "../stores/air-traffic-control.js";
	import { required } from "../util/validators.js";

	const statuses = [
    { name: 'Departed Gate', value: 'DEPARTED_GATE' },
    // { name: 'Outbouind Taxiing', value: 'OUTBOUND_TAXIING' },
		// { name: 'In Flight Line', value: 'IN_FLIGHT_LINE' },
		// { name: 'Cleared For Take Off', value: 'CLEARED_FOR_TAKEOFF' },
		{ name: 'In Flight', value: 'IN_FLIGHT' },
		// { name: 'Landing Pattern', value: 'LANDING_PATTERN' },
		// { name: 'Cleared For Landing', value: 'CLEARED_FOR_LANDING' },
		{ name: 'Landed', value: 'LANDED' },
		// { name: 'Arrived At Gate', value: 'ARRIVED_AT_GATE' },
	]
	let isDialogActive = false;
	let formData = {
		aircraftId: "05e5b41c-1fc7-4946-b04a-fb7a43d9d119",
		number: 1983,
		tailNumber: 2011,
		equipment: "RPC AC INTERNATIONAL"
	}

	onMount(async () => {
		$controls = await Api.get("/traffic-control/");
	})

	const submit= async () => {
		const res = await Api.post("/traffic-control/", formData);
		if (res) {
			$controls = [...$controls, res]
			toggleDialog()
		}
	}

	const updateStatus = async (status, control) => {
		let data = {
			number: control.number,
			status: status
		};
		const res = await Api.patch(`/traffic-control/${control.aircraftId}/status`, data);
	}

	const toggleDialog = () => {
		isDialogActive = !isDialogActive;
	}

	$: aircrafts = $inventories.map((inv => {
		return {
			name: inv.manufacturerSpecification.manufacturer,
			value: inv.id,
			...inv
		};
	}));

	$: valid = Object.values(formData).every(f => !!f);
</script>

<CardForm title="Air Traffic Control" prevLink="airport-terminal" nextLink="aircraft-monitoring" isNextDisabled={$controls.length < 1}>
	<table class="mb-6">
		<thead>
			<tr>
				<th>#</th>
				<th>Number</th>
				<th>Tail Number</th>
				<th>Equipment</th>
				<th>Status</th>
				<th>#</th>
			</tr>
		</thead>
		<tbody>
			{#each $controls as control, ind (control.id)}
				<tr>
					<td>{ind + 1}</td>
					<td>{control.number}</td>
					<td>{control.tailNumber}</td>
					<td>{control.equipment}</td>
					<td class="table-select">
						<VlSelect on:change={(e) => updateStatus(e.detail, control)} solo items={statuses} bind:value={control.status} />
					</td>
					<td></td>
				</tr>
			{/each}
		</tbody>
	</table>
	<Button on:click={toggleDialog}>New Control</Button>
	<Dialog persistent class="pa-8" bind:active={isDialogActive}>
		<form on:submit|preventDefault={submit} style="min-height: 500px">
			<Select outlined rules={[required]} items={aircrafts} bind:value={formData.aircraftId}>Flight</Select>
			<TextField outlined rules={[required]} bind:value={formData.number}>Number</TextField>
			<TextField outlined rules={[required]} bind:value={formData.tailNumber}>Tail Number</TextField>
			<TextField outlined rules={[required]} bind:value={formData.equipment}>Equipment</TextField>
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

<style global lang="scss">
.table-select {
	.s-input, .s-input__slot {
		margin-bottom: 0;
	}
}
</style>
