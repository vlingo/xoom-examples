<svelte:head>
	<title>Flight Planning</title>
</svelte:head>

<script>
	import { TextField, Select, Button, Dialog, Row, Alert, Icon } from 'svelte-materialify/src';
	import CardForm from '../components/CardForm.svelte';
	import { Api } from "../api";
	import { flights } from "../stores/flights.js";
	import { inventories } from "../stores/inventory.js";
	import { required } from "../util/validators.js";

	let isDialogActive = false;
	let valid = false;
	let selectedAircraft = "";

	let formData = {
		schedule: {
			departure: {
				airport: {
					code: "TTAB789",
					plannedFor: 1613066697198
				}
			},
			arrival: {
				airport: {
					code: "TTAB12",
					plannedFor: 1613066797198
				}
			}
		},
		manufacturerSpecification: {
			manufacturer: "EMBRAER",
			model: "E2-190",
			serialNumber: "283201230"
		},
		carrier: {    
			name: "TAM",    
			type: "AIRLINE"
		}
	}

	const submit= async () => {
		const res = await Api.post("/flight-plannings/", formData);
		if (res) {
			$flights = [...$flights, res]
		}
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

	$: {
		if (selectedAircraft) {
			const { manufacturerSpecification, carrier } = aircrafts.find(airc => airc.id === selectedAircraft);
			formData.manufacturerSpecification = manufacturerSpecification;
			formData.carrier = carrier;
		}
	}

	$: valid = !!selectedAircraft && !!formData.schedule.departure.airport.code && !!formData.schedule.departure.airport.plannedFor && !!formData.schedule.arrival.airport.code && !!formData.schedule.arrival.airport.plannedFor;
</script>

<CardForm title="Flight Planning" prevLink="inventory" nextLink="fleet-crew" isNextDisabled={$flights.length < 1}>
	<table class="mb-6">
		<thead>
			<tr>
				<th>#</th>
				<th>Departure Airport Code</th>
				<th>Arrival Airport Code</th>
				<th>Cancelled</th>
				<th>#</th>
			</tr>
		</thead>
		<tbody>
			{#each $flights as flight, ind (flight.id)}
				<tr>
					<td>{ind + 1}</td>
					<td>{flight.schedule.departure.airport.code}</td>
					<td>{flight.schedule.arrival.airport.code}</td>
					<td>{flight.cancelled}</td>
					<td></td>
				</tr>
			{/each}
		</tbody>
	</table>
	{#if $flights.length < 1}
		<Alert class="error-color">
			<div slot="icon">
				<Icon class="mdi mdi-alert" />
			</div>
			<div>
				There is no flight planning! Add one.
			</div>
		</Alert>
	{/if}
	<Button on:click={toggleDialog}>New Flight</Button>
	<Dialog persistent class="pa-8" bind:active={isDialogActive}>
		<form on:submit|preventDefault={submit}>
			<Select outlined rules={[required]} items={aircrafts} bind:value={selectedAircraft}>Aircraft</Select>
			<TextField outlined rules={[required]} bind:value={formData.schedule.departure.airport.code}>Departure Airport Code</TextField>
			<TextField outlined rules={[required]} bind:value={formData.schedule.departure.airport.plannedFor}>Departure Airport Planned For</TextField>
			<TextField outlined rules={[required]} bind:value={formData.schedule.arrival.airport.code}>Arrival Airport Code</TextField>
			<TextField outlined rules={[required]} bind:value={formData.schedule.arrival.airport.plannedFor}>Arrival Airport Planned For</TextField>
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