<svelte:head>
	<title>Airport Terminal</title>
</svelte:head>

<script>
	import CardForm from '../components/CardForm.svelte';
	import { TextField } from 'svelte-materialify/src';
	import { Api } from "../api";
	import { terminals } from "../stores"
	import { onMount } from 'svelte';
	import { required } from "../util/validators.js";

	let valid = false;
	let cardForm;

	let formData = {
		number: "1983",
		gateAssignment: {
			terminal: "A1",
			number: "12"
		},
		equipment: {
			carrier: "EMB",
			tailNumber: "2011"
		},
		schedule: {
			scheduledDeparture: 1613066697198,
			scheduledArrival: 1613066908474,
			departureStatus: {
				actual: 1613066918474
			}
		}
	}
	const submit= async () => {
		const res = await Api.post("/flights/", formData);
		if (res) {
			$terminals = [...$terminals, res]
			cardForm.toggleDialog();
		}
	}

	onMount(async () => {
		$terminals = await Api.get("/flights/");
	})

	$: valid =  !!formData.number &&
							!!formData.equipment.carrier &&
							!!formData.equipment.tailNumber &&
							!!formData.gateAssignment.terminal &&
							!!formData.gateAssignment.number &&
							!!formData.schedule.scheduledDeparture &&
							!!formData.schedule.scheduledArrival &&
							!!formData.schedule.departureStatus.actual;

</script>

<CardForm
	title="Airport Terminal"
	buttonText="New Terminal"
	prevLink="fleet-crew"
	nextLink="air-traffic-control"
	isNextDisabled={$terminals.length < 1}
	showNoContent={$terminals.length < 1}
	{valid}
	on:submit={submit}
	bind:this={cardForm}
>
	<table class="mb-6">
		<thead>
			<tr>
				<th>#</th>
				<th>Number</th>
				<th>Gate <small>(Terminal - Number)</small></th>
				<th>Equipment <small>(Carrier - Tail Number)</small></th>
				<th>Scheduled Departure</th>
				<th>Scheduled Arrival</th>
				<th>Departure Status</th>
				<th>#</th>
			</tr>
		</thead>
		<tbody>
			{#each $terminals as terminal, ind (terminal.id)}
				<tr>
					<td>{ind + 1}</td>
					<td>{terminal.number}</td>
					<td>{terminal.gateAssignment.terminal} - {terminal.gateAssignment.number}</td>
					<td>{terminal.equipment.carrier} - {terminal.equipment.tailNumber}</td>
					<td>{terminal.schedule.scheduledDeparture}</td>
					<td>{terminal.schedule.scheduledArrival}</td>
					<td>{terminal.schedule.departureStatus.actual}</td>
					<td></td>
				</tr>
			{/each}
		</tbody>
	</table>

	<div slot="dialog-form">
		<TextField outlined rules={[required]} bind:value={formData.number}>Number</TextField>

		<TextField outlined rules={[required]} bind:value={formData.equipment.carrier}>Equipment - Carrier</TextField>
		<TextField outlined rules={[required]} bind:value={formData.equipment.tailNumber}>Equipment - Tail Number</TextField>

		<TextField outlined rules={[required]} bind:value={formData.gateAssignment.terminal}>Gate Assignment - Terminal</TextField>
		<TextField outlined rules={[required]} bind:value={formData.gateAssignment.number}>Gate Assignment - Number</TextField>

		<TextField outlined rules={[required]} bind:value={formData.schedule.scheduledDeparture}>Scheduled Departure</TextField>
		<TextField outlined rules={[required]} bind:value={formData.schedule.scheduledArrival}>Scheduled Arrival</TextField>
		<TextField outlined rules={[required]} bind:value={formData.schedule.departureStatus.actual}>Departure Status - Actual</TextField>
	</div>
</CardForm>

