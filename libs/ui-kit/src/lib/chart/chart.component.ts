import { Component, input, output, AfterViewInit, OnDestroy, ElementRef, inject, ViewEncapsulation, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as echarts from 'echarts/core';
import { BarChart, LineChart, PieChart, ScatterChart } from 'echarts/charts';
import { GridComponent, TooltipComponent, LegendComponent, DataZoomComponent } from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import { EChartsOption } from 'echarts';

echarts.use([BarChart, LineChart, PieChart, ScatterChart, GridComponent, TooltipComponent, LegendComponent, DataZoomComponent, CanvasRenderer]);

export type ChartType = 'bar' | 'line' | 'pie' | 'scatter';

@Component({
  selector: 'avgc-chart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class ChartComponent implements AfterViewInit, OnDestroy {
  private el = inject(ElementRef);
  private chart: echarts.ECharts | null = null;

  options = input<EChartsOption>({});
  type = input<ChartType>('bar');
  height = input<string>('400px');
  loading = input<boolean>(false);
  chartClick = output<{ name: string; value: number }>();

  ngAfterViewInit(): void {
    const dom = this.el.nativeElement.querySelector('.avgc-chart__container');
    if (!dom) return;
    this.chart = echarts.init(dom);
    this.chart.setOption(this.buildOption());
    this.chart.on('click', (params: any) => this.chartClick.emit({ name: params.name, value: params.value }));
    effect(() => { if (this.chart) { this.chart.setOption(this.buildOption(), true); } });
    const ro = new ResizeObserver(() => this.chart?.resize());
    ro.observe(dom);
  }

  private buildOption(): EChartsOption {
    const base: EChartsOption = {
      tooltip: { trigger: 'axis', confine: true, backgroundColor: '#FFFFFF', borderColor: '#E2E8F0', textStyle: { color: '#0F172A', fontSize: 13 } },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      textStyle: { fontFamily: "'Noto Sans', 'Noto Sans Tamil', sans-serif" },
    };
    return { ...base, ...this.options() };
  }

  ngOnDestroy(): void { this.chart?.dispose(); }
}
