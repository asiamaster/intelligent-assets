<table id="grid" data-toggle="table" data-title="库存列表" class="table" data-toolbar="#toolbar" data-pagination="true" data-page-number="1" data-page-size="5"
                   data-query-params="queryParams" data-side-pagination="server" data-method="POST" data-content-type="application/x-www-form-urlencoded" data-single-select="true"
                   data-click-to-select="true"  data-checkbox-header="true" data-unique-id="id" data-sort-name="id" data-sort-order="desc"  data-icons="bui.variable.icons" data-buttons-class="primary"
                   data-show-refresh="true" data-show-fullscreen="true" data-show-columns="true">
                <thead>
                 <tr>
                    <th data-radio="true"></th>
                    <th data-field="code" data-sortable="true" data-align="center" data-width-unit="%" data-formatter="codeFormatter" >
                        出库单号
                    </th>
                    <th data-field="customerName" data-sortable="true" data-sort-name="customer_name" data-align="center">
                        客户名称
                    </th>
                    <th data-field="customerCellphone" data-sortable="true" data-align="center" >
                        联系电话
                    </th>
                    <th data-field="departmentName" data-sortable="true" data-align="center">
                       部门
                    </th>
                    <th data-field="districtName" data-sortable="true" data-align="center" data-sort-name="type" >
                        冷库区域
                    </th>
                    <th data-field="assetsCode" data-sortable="true" data-align="center">
                       冷库编号
                    </th>
                     <th data-field="categoryName" data-sortable="true" data-sort-name="category_name" data-align="center">
                        货物品类
                    </th>
                    <th data-field="quantity" data-sortable="true" data-sort-name="quantity" data-align="center">
                        出库件数
                    </th>
                    <th data-field="stockOutDate" data-sortable="true" data-sort-name="weight" data-align="center" data-provider="dateProvider">
                        出库日期
                    </th>
                     <th data-field="creator" data-sortable="true" data-sort-name="weight" data-align="center" >
                       操作员
                    </th>
                     <th data-field="notes" data-sortable="true" data-sort-name="weight" data-align="center" >
                        备注
                    </th>
                </tr>
                </thead>
            </table>